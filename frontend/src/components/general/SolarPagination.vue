<script>
export default {
  name: "SolarPagination",
  props: {
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    }
  },
  methods: {
    prevPage() {
      this.$emit('previous');  // Emit the input value
    },
    nextPage() {
      this.$emit('next');
    }
  }
}
</script>

<template>
  <div class="flex items-center justify-end gap-3 p-3">
    <button @click="prevPage" class="pagination-btn">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" viewBox="0 0 20 20" fill="white">
        <path fill-rule="evenodd" clip-rule="evenodd"
              d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"/>
      </svg>
    </button>
    <p>{{ currentPage }}<span class="mx-0.25">/</span>{{ totalPages }}</p>
    <button @click="nextPage" class="pagination-btn">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" viewBox="0 0 20 20" fill="white">
        <path fill-rule="evenodd" clip-rule="evenodd"
              d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"/>
      </svg>
    </button>
  </div>
</template>

<style scoped>
.pagination-btn {
  display: flex;
  height: 2rem;
  width: 2rem;
  align-items: center;
  justify-content: center;
  border-radius: 0.25rem;
  background-color: #C7D02C;
  color: #333333;
}

.pagination-btn:hover {
  background-color: #a3b825;
}

</style>
