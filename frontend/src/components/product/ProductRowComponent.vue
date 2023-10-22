<script>
export default {
  name: "ProductRowComponent",
  props: {
    product: {
      type: Object,
      required: true,
    },
    isChecked: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      checked: this.isChecked,
      screenWidth: innerWidth,
    }
  },
  methods: {
    emitEdit() {
      this.$emit("edit", this.product);
    },
    emitDelete() {
      this.$emit("delete", this.product);
    },
    emitToggle() {
      this.$emit("toggle", this.checked);
    },
    maxTextLength(text, maxLength) {
      const suffix = "...";
      if (maxLength <= suffix.length) {
        return suffix;
      }
      if (text.length > maxLength) {
        return text.substring(0, maxLength - suffix.length) + suffix;
      }
      return text;
    },
    onResize() {
      this.screenWidth = innerWidth;
    },
  },
  computed: {
    screenSizeFactor() {
      if (this.screenWidth < 640) {
        return 1;
      }
      if (this.screenWidth < 768) {
        return 2;
      }
      if (this.screenWidth < 1024) {
        return 3;
      }
      if (this.screenWidth < 1280) {
        return 4;
      }
      if (this.screenWidth < 1536) {
        return 5;
      }
      return 6;
    }
  },
  mounted() {
    this.$nextTick(() => {
      addEventListener('resize', this.onResize);
    })
  },
  beforeUnmount() {
    removeEventListener('resize', this.onResize);
  },

}
</script>

<template>
  <tr class="border-gray-100 border-b hover:bg-gray-100">
    <!-- Check box  -->
    <td class="w-4 pl-4">
      <div class="flex">
        <input
            type="checkbox"
            :checked="checked" v-model="checked" @change="emitToggle"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2">
      </div>
    </td>

    <!--Product Items -->
    <td class="pl-6 text-gray-900 whitespace-nowrap ">
      <div class="text-base font-medium">{{ maxTextLength(product['name'], 10 * screenSizeFactor) }}</div>
      <div class="font-light text-gray-500">{{ maxTextLength(product['description'], 10 * screenSizeFactor) }}</div>
    </td>

    <td class="px-6 py-4">€{{ product['price'].toFixed(2) }}</td>

    <!-- Edit/Delete Product -->
    <td class="px-6 py-4 row">
      <div @click="emitEdit" class="font-medium text-blue-700 cursor-pointer hover:underline">Edit product</div>
      <div @click="emitDelete" class="font-medium text-red-600 cursor-pointer hover:underline">Delete product</div>
    </td>

  </tr>
</template>

<style scoped>

</style>
