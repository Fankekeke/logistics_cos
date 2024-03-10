<template>
  <a-modal v-model="show" title="提现记录详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="audit(2)" type="danger">
        驳回
      </a-button>
      <a-button key="back" @click="audit(1)">
        通过
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="withdrawData !== null">
      <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
        <a-steps :current="current" progress-dot size="small">
          <a-step title="已提交" />
          <a-step title="正在审核" />
          <a-step :title="currentText" />
        </a-steps>
      </div>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>员工编号：</b>
          {{ withdrawData.code }}
        </a-col>
        <a-col :span="8"><b>员工姓名：</b>
          {{ withdrawData.name ? withdrawData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ withdrawData.phone ? withdrawData.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>提现金额：</b>
          {{ withdrawData.withdrawPrice }} 元
        </a-col>
        <a-col :span="8"><b>提现后余额：</b>
          {{ withdrawData.accountPrice }} 元
        </a-col>
        <a-col :span="8"><b>申请时间：</b>
          {{ withdrawData.createDate }}
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
  name: 'withdrawView',
  props: {
    withdrawShow: {
      type: Boolean,
      default: false
    },
    withdrawData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.withdrawShow
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
      userInfo: null,
      current: 0,
      currentText: '审核结果'
    }
  },
  watch: {
    withdrawShow: function (value) {
      if (value) {
        if (this.withdrawData.status == 0) {
          this.current = 1
        }
        if (this.withdrawData.status == 1) {
          this.current = 2
          this.currentText = '审核完成'
        }
        if (this.withdrawData.status == 2) {
          this.current = 2
          this.currentText = '审核驳回'
        }
        this.imagesInit(this.withdrawData.images)
      }
    }
  },
  methods: {
    audit (status) {
      let data = this.withdrawData
      data.status = status
      this.$post(`/cos/order-info/auditWithdraw`, data).then((r) => {
        this.$emit('auditSuccess')
      })
    },
    local (withdrawData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(withdrawData.longitude, withdrawData.latitude)
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
