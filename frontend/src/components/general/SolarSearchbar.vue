<script>
export default {
  name: "SolarSearchbar",
  props: {
    placeHolder: {
      type: String,
      required: true,
    },
  },
  methods: {
    onInput(event) {
      this.$emit('search', event.target.value);  // Emit the input value
    }
  }

}
</script>

<template>
  <div>
    <div class="searchbar">
      <div class="searchbar-container">
        <svg class="search-icon" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
      </div>
      <input type="text" class="search-input" :placeholder="placeHolder" @keyup="onInput">
    </div>
  </div>

</template>

<style scoped>
.searchbar {
  position: relative;
}

.searchbar-container {
  position: absolute;
  display: flex;
  top: 0;
  bottom: 0;
  align-items: center;
  padding-left: 0.75rem;
  pointer-events: none;
}

.search-icon {
  width: 1rem;
  height: 1rem;
  color: rgb(107 114 128);
}

.search-input {
  display: block;
  padding: 0.5rem 0.5rem 0.5rem 2.5rem;
  color: rgb(17 24 39);
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  border-width: 1px;
  border-color: rgb(209 213 219);
  border-radius: 0.5rem /* 8px */;
  width: 20rem;
  background-color: rgb(249 250 251);
}
</style>
