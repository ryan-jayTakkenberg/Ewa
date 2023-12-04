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
  <div class="relative ml-2 mr-2">
    <button type="button" class="solar-button" :disabled="disabled">{{ buttonText }}</button>
  </div>
</template>

<style scoped>

.solar-button {
  height: 42px;
  color: white;
  background-color: #c7d02c;
  font-weight: 500;
  border-radius: 0.5rem;
  padding: 0.5rem 1.0rem;;
  display: inline-flex;
  align-items: center;
  font-size: 0.875rem/* 14px */;
  line-height: 1.25rem/* 20px */
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
