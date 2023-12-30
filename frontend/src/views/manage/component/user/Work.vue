<template>
  <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
    <div style="height: 850px;">
      <div style="height: 750px;padding: 0 50px">
        <div style="font-size: 35px;font-weight: 500;color: white;font-family: SimHei">Hello</div>
        <div style="font-size: 22px;font-weight: 500;color: white;font-family: SimHei">配送下单</div>
        <div style="height: 680px;margin-top: 50px">
          <a-card :bordered="false" hoverable style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff;padding-bottom: 30px">
            <a-row style="padding: 50px;margin: 0 auto">
              <a-col :span="24">
                <a-row>
                  <a-col :span="8">
                    <a-input v-model="key" placeholder="配送地址"/>
                  </a-col>
                  <a-col :span="8" :offset="1">
                    <a-input v-model="key" placeholder="收货地址"/>
                  </a-col>
                  <a-col :span="24"></a-col>
                  <a-col :span="24" style="font-size: 15px;font-family: SimHei;">
                    <div style="margin-top: 30px">
                      <a-form :form="form" layout="vertical">
                        <a-row :gutter="20">
                          <a-col :span="5">
                            <a-form-item label='订单名称' v-bind="formItemLayout">
                              <a-input v-decorator="[
                                'contactPerson',
                                { rules: [{ required: true, message: '请输入订单名称!' }] }
                                ]"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="5">
                            <a-form-item label='物品类型' v-bind="formItemLayout">
                              <a-select v-decorator="[
                                'goodsType',
                                { rules: [{ required: true, message: '请输入物品类型!' }] }
                                ]">
                                <a-select-option value="1">文件</a-select-option>
                                <a-select-option value="2">食品</a-select-option>
                                <a-select-option value="3">蛋糕</a-select-option>
                                <a-select-option value="4">数码</a-select-option>
                                <a-select-option value="5">证件</a-select-option>
                                <a-select-option value="6">药品</a-select-option>
                                <a-select-option value="7">海鲜</a-select-option>
                                <a-select-option value="8">鲜花</a-select-option>
                                <a-select-option value="9">服饰</a-select-option>
                                <a-select-option value="10">其他</a-select-option>
                              </a-select>
                            </a-form-item>
                          </a-col>
                          <a-col :span="5">
                            <a-form-item label='物品重量' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'weight',
                              { rules: [{ required: true, message: '请输入物品重量!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="5">
                            <a-form-item label='物品高度' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'height',
                              { rules: [{ required: true, message: '请输入物品高度!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="5">
                            <a-form-item label='物品宽度' v-bind="formItemLayout">
                              <a-input-number style="width: 100%" v-decorator="[
                              'width',
                              { rules: [{ required: true, message: '请输入物品宽度!' }] }
                              ]" :min="0.1" :step="0.1"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24">
                            <a-form-item label='备注' v-bind="formItemLayout">
                              <a-textarea :rows="4" v-decorator="[
                              'content',
                               { rules: [{ required: true, message: '请输入备注!' }] }
                              ]"/>
                            </a-form-item>
                          </a-col>
                          <a-col :span="24">
                            <a-form-item label='图册' v-bind="formItemLayout">
                              <a-upload
                                name="avatar"
                                action="http://127.0.0.1:9527/file/fileUpload/"
                                list-type="picture-card"
                                :file-list="fileList"
                                @preview="handlePreview"
                                @change="picHandleChange"
                              >
                                <div v-if="fileList.length < 8">
                                  <a-icon type="plus" />
                                  <div class="ant-upload-text">
                                    Upload
                                  </div>
                                </div>
                              </a-upload>
                              <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                                <img alt="example" style="width: 100%" :src="previewImage" />
                              </a-modal>
                            </a-form-item>
                          </a-col>
                        </a-row>
                        <a-button type="primary" @click="fetch">
                          下一步
                        </a-button>
                      </a-form>
                    </div>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
    <Map :orderData="orderMapView.merchantInfo"
         @close="handleorderMapViewClose"
         :orderShow="orderMapView.visiable">
    </Map>
  </div>
</template>

<script>

import {mapState} from 'vuex'
import VehicleView from './VehicleView.vue'
import Map from './Map.vue'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Work',
  components: {Map, VehicleView},
  data () {
    return {
      fileList: [],
      previewVisible: false,
      previewImage: '',
      formItemLayout,
      form: this.$form.createForm(this),
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      key: '',
      roomList: [],
      roomTypeList: [],
      loading: false,
      vehicleView: {
        visiable: false,
        data: null
      },
      rentView: {
        visiable: false,
        data: null
      },
      startDate: null,
      endDate: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
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
    handlevehicleViewClose () {
      this.vehicleView.visiable = false
    },
    handlevehicleViewSuccess () {
      this.vehicleView.visiable = false
      this.$message.success('添加订单成功')
    },
    orderSuccess () {
      this.rentView.visiable = false
      this.$message.success('添加订单成功')
      this.fetch()
    },
    collectDelete (id) {
      this.$delete(`/cos/collect-info/${id}`).then((r) => {
        this.$message.success('取消收藏成功')
        this.fetch()
      })
    },
    collectAdd (roomCode) {
      let data = { userId: this.currentUser.userId, roomCode }
      this.$post(`/cos/collect-info`, data).then((r) => {
        this.$message.success('成功')
        this.fetch()
      })
    },
    view (record) {
      // if (record.currentStatus === '0') {
      //   this.$message.warn('此商家不在营业时间内')
      //   return false
      // }
      this.orderMapView.merchantInfo = record
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    getRoomType () {
      this.$get(`/cos/vehicle-type-info/list`).then((r) => {
        this.roomTypeList = r.data.data
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/order-info/selectMerchantList`, { key: this.key }).then((r) => {
        this.roomList = r.data.data
      })
    },
    fetch () {
      this.getWorkStatusList()
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
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
