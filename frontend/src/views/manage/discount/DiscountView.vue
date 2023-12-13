<template>
  <a-modal v-model="show" title="优惠券详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="discountData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">优惠券信息</span></a-col>
        <a-col :span="8"><b>优惠券编号：</b>
          {{ discountData.code }}
        </a-col>
        <a-col :span="8"><b>优惠券名称：</b>
          {{ discountData.couponName ? discountData.couponName : '- -' }}
        </a-col>
        <a-col :span="8"><b>优惠券类型：</b>
          <span v-if="discountData.type == 1">满减</span>
          <span v-if="discountData.type == 2">折扣</span>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountData.type == 1">
        <a-col :span="8"><b>满减金额：</b>
          {{ discountData.discountPrice }} 元
        </a-col>
        <a-col :span="8"><b>门槛金额：</b>
          {{ discountData.threshold }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountData.type == 2">
        <a-col :span="8"><b>折扣：</b>
          {{ discountData.rebate }} 折
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>备注：</b>
          {{ discountData.content }}
        </a-col>
        <a-col :span="8"><b>发放时间：</b>
          {{ discountData.createDate }}
        </a-col>
        <a-col :span="8"><b>状态：</b>
          <span v-if="discountData.status == 0" style="color: green">未使用</span>
          <span v-if="discountData.status == 1" style="color: red">已使用</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属用户：</b>
          {{ discountData.userName }}
        </a-col>
        <a-col :span="8"><b>邮箱地址：</b>
          {{ discountData.mail }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ discountData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图册</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
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
export default {
  name: 'discountView',
  props: {
    discountShow: {
      type: Boolean,
      default: false
    },
    discountData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.discountShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    discountShow: function (value) {
      if (value) {
        this.imagesInit(this.discountData.images)
      }
    }
  },
  methods: {
    local (discountData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(discountData.longitude, discountData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
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
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
