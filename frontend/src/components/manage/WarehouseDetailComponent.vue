<template>
  <div class="box">
    <div class="content">
      <div class="content-header"></div>
      <div class="content-box">
        <div class="content-box-text">
          <table>
            <thead>
            <tr><th>List of all the warehouses</th></tr>
            </thead>
            <tbody>
            <tr v-for="warehouse in warehouseList" :key="warehouse.name" @click="selectWarehouse(warehouse)" :class="{ 'selected': selectedWarehouse === warehouse }">
              {{"Name: " + warehouse.name + ", Address: " + warehouse.address + ", " + warehouse.postalCode + ", " + warehouse.city}}
            </tr>
            </tbody>
          </table>
          <button @click="showEditComponent = !showEditComponent" v-if="!selectedWarehouse">Create a new Warehouse</button>
          <button @click="removeWarehouse()" v-if="selectedWarehouse">Remove selected Warehoue</button>
        </div>
      </div>
    </div>
  </div>
  <WarehouseEditComponent
      v-if="showEditComponent"
      @cancel="handleCancel"
      :name="selectedWarehouse ? selectedWarehouse.name : ''"
      :address="selectedWarehouse ? selectedWarehouse.address : ''"
      :postalCode="selectedWarehouse ? selectedWarehouse.postalCode : ''"
      :city="selectedWarehouse ? selectedWarehouse.city : ''"
      @add-warehouse="addWarehouse"
      @save-warehouse="saveWarehouse"
  />
</template>

<script>
import Warehouse from "@/models/warehouse";
import WarehouseEditComponent from "@/components/manage/WarehouseEditComponent";

export default {
  name: 'WarehouseDetailComponent',
  components: {
    WarehouseEditComponent,
  },
  data(){
    return{
      warehouseList: [...Warehouse.warehouses],
      warehouseNumber: 0,
      selectedWarehouse: null,
      showEditComponent: false,
    };
  },
  created() {
    if (!this.warehouseList?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!Warehouse.fetching) {
          this.warehouseList = [...Warehouse.warehouses];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
  },
  methods: {
    handleCancel(){
      this.showEditComponent = false;
      this.selectedWarehouse = null;
    },
    selectWarehouse(warehouse){
      if (this.selectedWarehouse === warehouse){
        this.handleCancel()
      } else {
        this.selectedWarehouse = warehouse;
        this.showEditComponent = true;
      }
    },
    removeWarehouse(){
      const index = this.warehouseList.indexOf(this.selectedWarehouse);
      if (index !== -1) {
        this.warehouseList.splice(index, 1);
        this.handleCancel()
      }
    },
    addWarehouse(newWarehouseData) {
      const name = newWarehouseData.name;
      const address = newWarehouseData.address;
      const postalCode =newWarehouseData.postalCode;
      const city = newWarehouseData.city;
      const newWarehouse = new Warehouse(name, address, postalCode,city);
      this.warehouseList.push(newWarehouse);
      this.handleCancel();
    },
    saveWarehouse(updatedWarehouseData) {
      if (this.selectedWarehouse) {
        this.selectedWarehouse.name = updatedWarehouseData.name;
        this.selectedWarehouse.address = updatedWarehouseData.address;
        this.selectedWarehouse.postalCode = updatedWarehouseData.postalCode;
        this.selectedWarehouse.city = updatedWarehouseData.city;

      }
    }
  },
}
</script>

<style scoped>
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

.content-header{
  height: 175px;
  background: url("../../../static/images/manageDetailHeader3.jpg") center no-repeat;
  background-size: cover;
  border-radius: 20px 20px 0 0 ;
}

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

table{
  border-collapse: collapse;
  width: 100%;
}

tr{
  border: 1px solid black;
  padding: 10px;
}

tr:hover{
  color: #818181;
  cursor: pointer;
}

.selected{
  background: #c7d02c;
}
</style>