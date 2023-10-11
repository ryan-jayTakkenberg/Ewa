<script>
export default {
  name: "SolarButton",
  props: {
    buttonText: {
      type: String,
      required: true,
    },
    onClick: {
      type: Function,
      required: true,
    }
  }
}
</script>

<template>
  <button type="button" class="solar-button">{{ buttonText }}</button>
</template>

<style scoped>
.solar-button {
  color: white;
  background-color: grey;
  font-weight: 500;
  border-radius: 0.5rem /* 8px */;
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  padding: 0.625rem 1.25rem;;
  margin-right: 0.5rem;
  margin-left: 0.5rem;
}

.solar-button:hover {
  background-color: #c7d02c;
}

.solar-button:focus {
  background-color: #c7d02c;
}

</style>
