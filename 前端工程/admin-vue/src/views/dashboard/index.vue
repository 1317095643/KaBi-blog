<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      卡比博客管理系统
    </div>
    <div>
      <img src="../img/kabi.png">
    </div>
    <div>
      联系方式:1317095643(QQ)
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
