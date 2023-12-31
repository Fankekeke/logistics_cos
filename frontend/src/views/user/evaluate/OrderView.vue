<template>
  <a-modal v-model="show" title="订单详情" @cancel="onClose" :width="1300">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
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
          {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }} 元
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
          {{ orderInfo.kilometre ? orderInfo.kilometre : '- -' }} KM
        </a-col>
        <a-col :span="6"><b>配送价格：</b>
          {{ orderInfo.distributionPrice ? orderInfo.distributionPrice : '- -' }} 元
        </a-col>
        <a-col :span="6"><b>折扣后价格：</b>
          {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice : '- -' }} 元
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
    <div style="font-size: 13px;font-family: SimHei" v-if="discountInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">优惠信息</span></a-col>
        <a-col :span="6"><b>优惠券编号：</b>
          {{ discountInfo.code }}
        </a-col>
        <a-col :span="6"><b>优惠券名称：</b>
          {{ discountInfo.couponName ? discountInfo.couponName : '- -' }}
        </a-col>
        <a-col :span="6"><b>优惠券类型：</b>
          <span v-if="discountInfo.type == 1">满减</span>
          <span v-if="discountInfo.type == 2">折扣</span>
        </a-col>
        <a-col :span="6"><b>发放时间：</b>
          {{ discountInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 1">
        <a-col :span="6"><b>满减金额：</b>
          {{ discountInfo.discountPrice }} 元
        </a-col>
        <a-col :span="6"><b>门槛金额：</b>
          {{ discountInfo.threshold }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 2">
        <a-col :span="6"><b>折扣：</b>
          {{ discountInfo.rebate }} 折
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>备注：</b>
          {{ discountInfo.content }}
        </a-col>
      </a-row>
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
    <div style="font-size: 13px;font-family: SimHei" v-if="evaluateInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span></a-col>
        <a-col :span="6"><b>评价分数：</b>
          <a-rate :default-value="evaluateInfo.score" disabled />
        </a-col>
        <a-col :span="6"><b>评价内容：</b>
          <a-tooltip>
            <template slot="title">
              {{ evaluateInfo.content}}
            </template>
            {{ evaluateInfo.content.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="6"><b>评价时间：</b>
          {{ evaluateInfo.createDate ? evaluateInfo.createDate : '- -' }}
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
    },
    columns () {
      return [{
        title: '菜品名称',
        dataIndex: 'dishesName'
      }, {
        title: '图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
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
      startAddressInfo: null,
      endAddressInfo: null,
      discountInfo: null,
      staffInfo: null,
      evaluateInfo: null
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
      this.$get(`/cos/order-info/evaluate/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.startAddressInfo = r.data.startAddress
        this.endAddressInfo = r.data.endAddress
        this.discountInfo = r.data.discount
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.evaluateInfo.images)
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
