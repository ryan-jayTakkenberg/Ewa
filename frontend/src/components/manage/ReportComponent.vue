<template>
  <div class="box">
    <div class="content">
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h1> Notifications </h1>
            <div class="notification-bell" @click="toggleNotifications">
              <i class="material-symbols-outlined">notifications</i>
              <div class="notification-count">{{ listArray.length }}</div>
            </div>
            <div v-if="showNotifications" class="notification-window">
              <h2>Notifications</h2>
              <ul>
                <li
                    v-for="(item, index) in listArray"
                    :key="index"
                    @click="toggleItemSelection(index, item)"
                    :class="{ selected: isSelected(index) }"
                >
                  {{ item }}
                </li>
              </ul>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <report-detail-component v-if="selectedItems.length > 0" :items="selectedItems" @read-notification="readNotification"></report-detail-component>
</template>

<script>
import ReportDetailComponent from "@/components/manage/ReportDetailComponent.vue";

export default {
  name: "ReportComponent",
  components: { ReportDetailComponent },
  data() {
    return {
      showNotifications: false,
      listArray: [
        "Zonnepanelen zijn bijna op, warheouse 4",
        "hout is bijna op, warehouse 2",
        "personeels tekort, warehouse 4",
      ],
      selectedItems: [], // Array to store selected items (indices)
    };
  },
  methods: {
    toggleNotifications() {
      this.showNotifications = !this.showNotifications;
    },
    toggleItemSelection(index, item) {
      // Toggle the selection of the clicked item
      if (this.isSelected(index)) {
        this.selectedItems.splice(this.selectedItems.findIndex((el) => el.index === index), 1);
      } else {
        this.selectedItems.push({ index, item });
      }
    },
    isSelected(index) {
      // Check if an item is selected
      return this.selectedItems.some((el) => el.index === index);
    },
    readNotification() {
      // Remove selected items when Detail is clicked
      this.listArray = this.listArray.filter((_, index) => !this.isSelected(index));
      this.selectedItems = []; // Clear selected items
    },
  },
};
</script>


<style >

h1{
  text-align: center;
}


.notification-bell {
  cursor: pointer;
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 33%;
}

.notification-bell i {
  font-size: 24px;
  margin-right: 10px; /* Ruimte tussen het bel-icoontje en de teller */
}

.notification-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 3px 8px;
  font-size: 14px;
}
.notification-window {
  border-top: 1px solid black;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.notification-window h2 {
  margin: 0 0 10px;
  font-size: 18px;
}
red {
  color: red;
}
.selected {
  background-color: #c7d02c; /* Set the background color for selected items */
}

.box{
  display: flex;
  justify-content: center;
  align-content: center;
}

.content{
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 15px 0 30px 0;
}

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

label{
  width: 100%;
  margin: 10px 0 5px 0;
}

input{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid rgb(128, 128, 128);
  width: 100%;
}


textarea{
  width: 100%;
  height: 125px;
  overflow: hidden;
  resize: none;
  padding: 5px;
  border-radius: 10px;
  border: 1px solid grey;
}

select{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid rgb(128, 128, 128);
  width: 100%;
}


button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

li{
  border: 1px solid black;
  margin: 5px;
  padding: 5px;
}

li:hover{
  background-color: #c7d02c;
}


@media (max-width: 500px) {
  .content{
    width: 70%;
  }
}

</style>