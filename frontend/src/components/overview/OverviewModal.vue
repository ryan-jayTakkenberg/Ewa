<template>

  <div class="background">
    <div class="modal">

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
  background: var(--col-white-100);
  border-radius: 10px;
  padding: 2rem;
  max-width: 500px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.description {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 1.5em;
  font-weight: var(--font-weight-fat);
  color: var(--col-black);
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
  background-color: var(--col-white-200);
  border-radius: 10px;
}

.reportContainer::-webkit-scrollbar-track {
  background-color: var(--col-white-150);
  border-radius: 10px;
}

.report {
  width: 100%;
  background: var(--col-white-100);
  border-radius: 5px;
  padding: 1rem;
  border: 2px solid var(--col-white-800);
}

.reportHeader {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid var(--col-white-200);
  width: 100%;
  margin-bottom: 0.5rem;
}

.reportSender {
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-small);
  color: var(--col-black);
}

.reportDate {
  font-weight: var(--font-weight-medium);
  font-size: var(--font-size-small);
  color: var(--col-white-500);
}

.reportBody {
  font-weight: var(--font-weight-medium);
  font-size: var(--font-size-small);
  color: var(--col-black)
}

.warningContainer {
  display: flex;
  align-items: center;
  justify-content: left;
  gap: 1rem;
  padding: 1rem;
  margin-top: 2rem;
  margin-bottom: 2rem;
  background: var(--col-warning-150);
  border-left: 5px solid var(--col-error)
}

.warningLogo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.warningText {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-medium);
  color: var(--col-black);
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
  background: var(--col-solar-500);
  font-weight: var(--font-weight-bold);
  color: var(--col-white-100);
}

.yesButton:hover {
  background: var(--col-solar-550);
}

.noButton {
  height: 50px;
  width: 100%;
  border-radius: 5px;
  border: 2px solid var(--col-white-150);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
}

.material-symbols-outlined.warning {
  color: var(--col-warning-500);
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

</style>