<template>
  <a-modal v-model="show" title="收益详情" @cancel="onClose" :width="1300">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="incomeInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">收益信息</span></a-col>
        <a-col :span="6"><b>订单收益：</b>
          {{ incomeInfo.income }} 元
        </a-col>
        <a-col :span="6"><b>配送费用：</b>
          {{ incomeInfo.deliveryPrice ? incomeInfo.deliveryPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>总收益：</b>
          {{ incomeInfo.totalPrice ? incomeInfo.totalPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>收益时间：</b>
          {{ incomeInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="userInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="6"><b>会员编号：</b>
          {{ userInfo.code }}
        </a-col>
        <a-col :span="6"><b>用户姓名：</b>
          {{ userInfo.name ? userInfo.name : '- -' }}
        </a-col>
        <a-col :span="6"><b>邮箱地址：</b>
          {{ userInfo.mail ? userInfo.mail : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系电话：</b>
          {{ userInfo.phone }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
        <a-col :span="6"><b>订单编号：</b>
          {{ orderInfo.code }}
        </a-col>
        <a-col :span="6"><b>订单名称：</b>
          {{ orderInfo.orderName ? orderInfo.orderName : '- -' }}
        </a-col>
        <a-col :span="6"><b>总价格：</b>
          {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>创建时间：</b>
          {{ orderInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>付款时间：</b>
          {{ orderInfo.payDate }}
        </a-col>
        <a-col :span="6"><b>公里数：</b>
          {{ orderInfo.kilometre ? orderInfo.kilometre : '- -' }}KM
        </a-col>
        <a-col :span="6"><b>配送价格：</b>
          {{ orderInfo.distributionPrice ? orderInfo.distributionPrice : '- -' }}元
        </a-col>
        <a-col :span="6"><b>折扣后价格：</b>
          {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice : '- -' }}元
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="startAddressInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">发货地址</span></a-col>
        <a-col :span="6"><b>详细地址：</b>
          {{ startAddressInfo.address }}
        </a-col>
        <a-col :span="6"><b>联系人：</b>
          {{ startAddressInfo.contactPerson ? startAddressInfo.contactPerson : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系方式：</b>
          {{ startAddressInfo.contactMethod ? startAddressInfo.contactMethod : '- -' }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="endAddressInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">送货地址</span></a-col>
        <a-col :span="6"><b>详细地址：</b>
          {{ endAddressInfo.address }}
        </a-col>
        <a-col :span="6"><b>联系人：</b>
          {{ endAddressInfo.contactPerson ? endAddressInfo.contactPerson : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系方式：</b>
          {{ endAddressInfo.contactMethod ? endAddressInfo.contactMethod : '- -' }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="staffInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">配送员信息</span></a-col>
        <a-col :span="6"><b>员工姓名：</b>
          {{ staffInfo.name }}
        </a-col>
        <a-col :span="6"><b>性别：</b>
          <span v-if="staffInfo.sex == '1'">男</span>
          <span v-if="staffInfo.sex == '2'">女</span>
        </a-col>
        <a-col :span="6"><b>员工工号：</b>
          {{ staffInfo.code }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
  </a-modal>
</template>

<script>
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
  name: 'orderView',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.orderShow
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
      current: 0,
      userInfo: null,
      orderInfo: null,
      incomeInfo: null,
      startAddressInfo: null,
      endAddressInfo: null,
      staffInfo: null
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
      }
    }
  },
  methods: {
    dataInit (orderId) {
      this.$get(`/cos/order-info/income/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.incomeInfo = r.data.income
        this.startAddressInfo = r.data.startAddress
        this.endAddressInfo = r.data.endAddress
        this.staffInfo = r.data.staff
        this.imagesInit(this.merchantInfo.images)
      })
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
