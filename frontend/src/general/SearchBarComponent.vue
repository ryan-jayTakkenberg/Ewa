<script>
export default {
  name: "SearchBarComponent",
  props: {
    placeholderText: {
      type: String,
      required: true,
    },
  }

}
</script>

<template>
  <form>
    <div class="search-bar">
      <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
        <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg"
             fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
      </div>
      <input type="search" id="default-search"
             class="block w-full p-4 pl-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500"
             placeholder="Search" required>
      <button type="submit" class="search-button">Search</button>
    </div>
  </form>
</template>

<style scoped>
.search-bar{
  position: relative;
  width: 600px;
}
.search-button {
  color: white;
  position: absolute;
  right: 0.625rem /* 10px */;
  bottom: 0.625rem /* 10px */;
  background-color: #c7d02c;;
  font-weight: 500;
  border-radius: 0.5rem /* 8px */;
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  padding: 0.5rem 1rem;;
}

.search-button:hover {
  background-color: #5B2E18;;

}

.search-button:focus {
  box-shadow: #c7d02c;
  outline: 2px solid transparent;
  outline-offset: 2px;
}
</style>
