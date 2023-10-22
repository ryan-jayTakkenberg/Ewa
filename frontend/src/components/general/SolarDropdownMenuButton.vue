<template>
  <div class="relative ml-2 mr-2">
    <!-- Dropdown button -->
    <button
        @click="toggleDropdown"
        @blur="hideDropdown"
        :class="{ 'dropdown-btn-open': isDropdownOpen, 'dropdown-btn': !isDropdownOpen }"
        :disabled="disabled">
      {{ textButton }}
      <svg class="w-2.5 h-2.5 ml-2.5" fill="none" viewBox="0 0 10 6">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 4 4 4-4"/>
      </svg>
    </button>

    <!-- Dropdown menu -->
    <div v-if="isDropdownOpen" class="absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44">
      <ul class="py-2 text-sm text-gray-700">
        <slot></slot>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "SolarDropdownMenuButton",
  props: {
    textButton: {
      type: String,
      required: true,
    },
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      isDropdownOpen: false
    };
  },
  methods: {
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
    },
    hideDropdown() {
      setTimeout(() => this.isDropdownOpen = false, 100);
    },
  }
};
</script>

<style scoped>
.dropdown-btn {
  color: white;
  background-color: #c7d02c;
  font-weight: 500;
  border-radius: 0.5rem /* 8px */;
  padding: 0.5rem 1.0rem;;
  text-align: center;
  display: inline-flex;
  align-items: center;
}

.dropdown-btn-open {
  color: white;
  background-color: #a3b825; /* Change to your desired open color */
  font-weight: 500;
  border-radius: 0.5rem;
  padding: 0.5rem 1.0rem;
  text-align: center;
  display: inline-flex;
  align-items: center;
}

.dropdown-btn:hover {
  background-color: #a3b825;
}

.dropdown-btn:disabled {
  opacity: 0.75;
}

.dropdown-btn:disabled:hover {
  background-color: #c7d02c;
}

</style>
