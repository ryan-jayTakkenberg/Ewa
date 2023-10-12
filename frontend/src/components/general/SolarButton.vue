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
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.5rem 1.0rem;;
  margin: 0 0.5rem;
  display: inline-flex;
  align-items: center;
  width: 105px;
  justify-content: center;
}

.solar-button:hover {
  background-color: #c7d02c;
}

.solar-button:focus {
  background-color: #c7d02c;
}

</style>
