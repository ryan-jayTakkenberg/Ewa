<template>

  <div class="main">


    <div class="overviewContainer">

      <!---------------------------------------------------------------------------------------->

      <div class="statsOverview">

        <div class="sectionTitle">Total Stock:</div>
        <div class="sectionDescription">View stock of all warehouses. Lorum ipsum text information lorum ipsum.</div>

        <div class="statsContainer">

          <div class="child">
            <div class="statTitle">Total</div>
            <div class="statValue">150</div>
          </div>

          <div class="child">
            <div class="statTitle">Solar Panels</div>
            <div class="statValue">48</div>
          </div>

          <div class="child">
            <div class="statTitle">Switches</div>
            <div class="statValue">35</div>
          </div>

          <div class="child">
            <div class="statTitle">Other</div>
            <div class="statValue">...</div>
          </div>

        </div>

      </div>

      <!---------------------------------------------------------------------------------------->

      <div class="warehouseOverview">

        <div class="sectionTitle">View stock per warehouse:</div>
        <div class="sectionDescription">Select a warehouse to view:</div>

        <div class="warehouseContainer">

          <div class="warehouseHeader">
            <select name="warehouses" v-model="selectedWarehouse">
              <option value="warehouse1">Warehouse 1</option>
              <option value="warehouse2">Warehouse 2</option>
              <option value="warehouse3">Warehouse 3</option>
              <option value="warehouse4">Warehouse 4</option>
            </select>
            <div class="closeBtn" @click="closeWarehouse" v-if="selectedWarehouse !== ''">
              <span class="material-symbols-outlined">close</span>
            </div>
          </div>

          <div class="warehouseContent" v-if="selectedWarehouse !== ''">CONTENT</div>
        </div>

      </div>

      <!---------------------------------------------------------------------------------------->

    </div>

  </div>

</template>

<script>

export default {
  name: "OverviewComponent",

  data() {
    return {
      selectedWarehouse: "",
    }
  },

  methods: {
    closeWarehouse() {
      this.selectedWarehouse = "";
    },
  },

};
</script>


<style scoped>

/* overview */
.overviewContainer {
  flex-direction: column;
  gap: 2rem;
  min-height: 100svh;
  width: calc(100% - 180px);
  padding: 2rem;
  float: right;
}

.statsOverview,
.warehouseOverview {
  padding-bottom: 2rem;
  border-bottom: 2px solid #e5e5e5;
}

.sectionTitle {
  font-size: 1em;
  font-weight: 600;
  color: #222;
}

.sectionDescription {
  font-size: 1em;
  font-weight: 300;
  width: 500px;
  color: #ccc;
}

.statsContainer {
  display: flex;
  gap: 5rem;
  height: auto;
  padding: 2rem;
  width: 100%;
  margin-top: 1rem;
  border-radius: 15px;
  background: #24313b;
}

.statsContainer .child {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 150px;
}

.statTitle {
  font-size: 20px;
  font-weight: 400;
  color: #fff;
}

.statValue {
  font-size: 70px;
  font-weight: 500;
  color: #fff;
}

.warehouseContainer {
  height: auto;
  padding: 1rem;
  width: 100%;
  margin-top: 1rem;
  background: none;
  border: 2px solid #212d37;
  border-radius: 15px;
  transition: 0.3s ease-in-out;
}

.warehouseHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

select {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px;
  padding: 0.5rem 1rem;
  font-weight: 500;
  border-radius: 10px;
  border: 1px solid #eee;
  outline: none;
  background: #f5f5f5;
}

.closeBtn {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

.closeBtn:hover {
  background: #f5f5f5;
}

.warehouseContent {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1rem;
  height: 500px;
  width: 100%;
  background: #c7d02c;
}

@media (max-width: 768px) {
  .overviewContainer {
    width: calc(100% - 70px);
  }
}

</style>
