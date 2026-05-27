<script>
export default {
  name:'MyTempPage',
  data(){
    return{
      msg:'Welcome to Your Vue.js.App',
      flag: true,
      number:10,
      inputType:'text',
      courses:['java','python','c++']
    }
  },
  methods:{
    numberAdd(){
      this.number=parseInt(this.number)+1
      if(this.number % 2==0){
        this.inputType='password'
        this.flag=false
      }else{
        this.inputType='text';
        this.flag=true
      }
    }
  },
  mounted() {
    alert("******************")
    this.inputType='text'
  }
}
</script>

<template>
  <h1>  {{"我的消息："+msg}}</h1>
  <div>{{flag?"我是真的":"我是假的"}}</div>
  <div v-if="flag">这是我需要显示的内容</div>
  <div v-show="!flag">我是v-show</div>
  {{number}}
  <input v-model="number" :type="inputType">
  <button @click="numberAdd()">number+1</button>
  <div>
    <ul>
      <li v-for="bean in courses">{{bean}}</li>
    </ul>
  </div>

  <el-button>按钮</el-button>

</template>

<style scoped>

</style>