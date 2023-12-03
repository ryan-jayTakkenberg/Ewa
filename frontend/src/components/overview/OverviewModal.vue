<template>

  <div class="background">
    <div class="modal">

<!--      <div class="header">-->
<!--        <div class="image"></div>-->
<!--      </div>-->

      <div class="description"> {{ description() }} </div>

      <div class="reportContainer">
        <div v-for="report in selectedReports" :key="report.id" class="report">

          <div class="reportHeader">
            <div class="reportSender"> {{ capitalizeFirstLetter(report.senderName) }} </div>
            <div class="reportDate"> {{ report.date }} </div>
          </div>

          <div class="reportBody"> {{ report.body }} </div>
        </div>


      </div>

      <div class="warningContainer">
        <div class="warningLogo"><span class="material-symbols-outlined warning">warning</span></div>
        <div class="warningText">{{ warningText() }}</div>
      </div>

      <div class="buttonWrapper">
        <button class="noButton" @click="cancelDelete">Cancel</button>
        <button class="yesButton" @click="confirmDelete">Delete</button>
      </div>

    </div>
  </div>

</template>

<script>
export default {

  props: {
    selectedReports: {
      type: Array,
      default: () => [],
    },
  },

  methods: {

    description() {
      const reportCount = this.selectedReports.length;
      return reportCount === 1
          ? "Are you sure you want to delete the following report?"
          : `Are you sure you want to delete the following reports?`;
    },

    warningText() {
      const reportCount = this.selectedReports.length;
      return reportCount === 1
          ? "Deleting this report will permanently remove it from your account."
          : `Deleting these reports will permanently remove them from your account.`;
    },

    confirmDelete() {
      this.$emit('confirm-delete', this.selectedReports);
    },

    cancelDelete() {
      this.$emit('cancel-delete');
    },

    capitalizeFirstLetter(str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    },
  },

};
</script>

<style scoped>

.background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(2px);
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.modal {
  background: #fff;
  border-radius: 10px;
  padding: 2rem;
  max-width: 500px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  margin-bottom: 3rem;
}

.image {
  height: 100px;
  width: 100px;
  background: url("../../../static/images/delete.png") center no-repeat;
  background-size: cover;
}

.description {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 1.5em;
  font-weight: 700;
  color: #222;
  margin-bottom: 2rem;
}

.reportContainer {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-left: 10px;
  padding-right: 5px;
  max-height: 250px;
  overflow-y: auto;
  border-radius: 5px;
}

.reportContainer::-webkit-scrollbar {
  width: 5px;
}

.reportContainer::-webkit-scrollbar-thumb {
  background-color: #e5e5e5;
  border-radius: 10px;
}

.reportContainer::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 10px;
}

.report {
  width: 100%;
  background: #fff;
  border-radius: 5px;
  padding: 1rem;
  border: 2px solid #555;
}

.reportHeader {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
  width: 100%;
  margin-bottom: 0.5rem;
}

.reportSender {
  font-weight: 600;
  font-size: 0.7em;
  color: #222;
}

.reportDate {
  font-weight: 500;
  color: #999;
  font-size: 0.7em;
}

.reportBody {
  font-weight: 500;
  font-size: 0.7em;
  color: #222;
}

.warningContainer {
  display: flex;
  align-items: center;
  justify-content: left;
  gap: 1rem;
  padding: 1rem;
  margin-top: 2rem;
  margin-bottom: 2rem;
  background: #fff3ea;
  border-left: 5px solid #fa5530;
}

.warningLogo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.warningText {
  font-size: 0.8em;
  font-weight: 500;
  color: #222;
}

.buttonWrapper {
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: space-between;
}

.yesButton {
  height: 50px;
  width: 100%;
  border-radius: 5px;
  background: #c5ce2c;
  font-weight: 600;
  color: #fff;
}

.noButton {
  height: 50px;
  width: 100%;
  border-radius: 5px;
  border: 2px solid #e5e5e5;
  font-weight: 600;
  color: #222
}

.material-symbols-outlined.warning {
  color: #b95848;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

</style>