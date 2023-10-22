<script>
export default {
  name: "SolarButton",
  props: {
    buttonText: {
      type: String,
      required: true,
    },
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  }
}
</script>

<template>
  <button type="button" class="solar-button" :disabled="disabled">{{ buttonText }}</button>
</template>

<style scoped>

.solar-button {
  color: white;
  background-color: #c7d02c;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.5rem 1.0rem;;
  margin: 0 0.5rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.solar-button:hover {
  background-color: #a3b825;
}

.solar-button:focus {
  background-color: #a3b825;
}

.solar-button:disabled {
  opacity: 0.75;
}

.solar-button:disabled:hover {
  background-color: #c7d02c;
}

</style>
