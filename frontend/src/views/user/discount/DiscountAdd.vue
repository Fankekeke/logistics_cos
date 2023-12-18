<template>
  <a-modal v-model="show" title="新增优惠券" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='优惠券名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'couponName',
            { rules: [{ required: true, message: '请输入优惠券名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='优惠券类型' v-bind="formItemLayout">
            <a-select @change="handleChange" v-decorator="[
              'type',
              { rules: [{ required: true, message: '请输入优惠券类型!' }] }
              ]">
              <a-select-option value="1">满减</a-select-option>
              <a-select-option value="2">折扣</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="discountType != null && discountType == 1">
          <a-form-item label='门槛金额' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'threshold',
            { rules: [{ required: true, message: '请输入门槛金额!' }] }
            ]" :min="0.1" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="discountType != null && discountType == 1">
          <a-form-item label='满减金额' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'discountPrice',
            { rules: [{ required: true, message: '请输入满减金额!' }] }
            ]" :min="0.1" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="discountType != null && discountType == 2">
          <a-form-item label='折扣' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'rebate',
            { rules: [{ required: true, message: '请输入折扣!' }] }
            ]" :min="0.1" :max="9.9" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='所属用户' v-bind="formItemLayout">
            <a-select style="width: 100%" v-decorator="[
              'userId',
              { rules: [{ required: true, message: '请输入所属用户!' }] }
              ]">
              <a-select-option v-for="(item, index) in userList" :value="item.id" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入优惠券描述!' }] }
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
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
  name: 'discountAdd',
  props: {
    discountAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.discountAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      userList: [],
      discountType: null
    }
  },
  mounted () {
    this.selectUserList()
  },
  methods: {
    handleChange (value) {
      this.discountType = value
      console.log(this.discountType)
    },
    selectUserList () {
      this.$get(`/cos/user-info/list`).then((r) => {
        this.userList = r.data.data
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          this.$post('/cos/discount-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
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

</style>
