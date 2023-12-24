<template>
  <div style="background:#ECECEC; padding:30px;width: 100%">
    <a-row :gutter="20">
      <a-col :span="6" v-for="(item, index) in orderList" :key="index" style="margin-bottom: 30px">
        <a-card hoverable style="width: 100%">
          <template slot="actions" class="ant-card-actions">
            <a-icon key="setting" type="setting" />
            <a-icon key="edit" type="edit" />
            <a-icon key="ellipsis" type="ellipsis" />
          </template>
          <a-card-meta :title="item.orderName">
            <div slot="description">
              <div>
                <a-icon type="user" />
                {{item.userName}}
                |
                <a-icon type="phone" />
                {{item.phone}}
                |
                <a-icon type="appstore" />
                <span v-if="item.goodsType == 1">文件</span>
                <span v-if="item.goodsType == 2">食品</span>
                <span v-if="item.goodsType == 3">蛋糕</span>
                <span v-if="item.goodsType == 4">数码</span>
                <span v-if="item.goodsType == 5">证件</span>
                <span v-if="item.goodsType == 6">药品</span>
                <span v-if="item.goodsType == 7">海鲜</span>
                <span v-if="item.goodsType == 8">鲜花</span>
                <span v-if="item.goodsType == 9">服饰</span>
                <span v-if="item.goodsType == 10">其他</span>
              </div>
              <div style="margin-top: 6px">
                <a-icon type="compass" />
                {{item.kilometre}}KM
                |
                <a-icon type="dollar" />
                {{item.afterOrderPrice}}元
              </div>
              <div style="margin-top: 6px">
                <a-icon type="clock-circle-o" />
                {{moment(item.createDate).format('YYYY-MM-DD HH:mm:ss')}}
              </div>
            </div>
            <a-avatar
              slot="avatar"
              shape="square"
              :src="'http://127.0.0.1:9527/imagesWeb/' + item.userImages"
            />
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'User',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      form: this.$form.createForm(this),
      formItemLayout,
      loading: false,
      courseInfo: [],
      dataLoading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      expertInfo: null,
      price: 0,
      orderList: []
    }
  },
  mounted () {
    this.getExpertInfo(this.currentUser.userId)
  },
  methods: {
    moment,
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.createDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: item.code})
        }
      })
      return listData || []
    },
    getExpertInfo (userId) {
      this.$get(`/cos/order-info/page`).then((r) => {
        this.orderList = r.data.data.records
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...expert}) {
      this.rowId = expert.id
      let fields = ['code', 'name', 'createDate', 'sex']
      let obj = {}
      Object.keys(expert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(expert['images'])
        }
        if (key === 'sex') {
          expert[key] = expert[key].toString()
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expert[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/staff-info', {
            ...values
          }).then((r) => {
            this.$message.success('更新成功')
            this.loading = false
            this.getExpertInfo(this.currentUser.userId)
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 12px;
    font-family: SimHei;
  }
</style>
