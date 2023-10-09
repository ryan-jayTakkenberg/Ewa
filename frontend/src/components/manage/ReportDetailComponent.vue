<template>
  <div class="box">
    <div class="content">
      <div class="content-box">
        <div class="content-box-text">
          <div>
            <h1> React </h1>
            <ul>
              <li v-for="(item, index) in items" :key="index">{{ item.item }}</li>
            </ul>
            <button id="readButton" @click="readNotification">Click if readed</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReportDetailComponent",
  props: {
    items: Array,
    selectedItems: Array,
  },
  methods: {
    readNotification() {
      this.$emit("read-notification", this.selectedItems);
    },
  },
};
</script>


<style>
#readButton{
  margin: 35px 20px 20px 70px;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

</style>
