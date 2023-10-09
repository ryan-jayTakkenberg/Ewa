<template>
  <table>
    <thead>
    <tr>
      <th v-for="attr of Object.keys(attributes)" :key="attr" @click="sort(attr)" :class="{'clickable': sortable}">
        <div class="d-inline-flex">
          {{ capitalizeText(attr) }}
          <span class="material-symbols-outlined" v-if="sortKey.name===attr && sortAsc">arrow_drop_up</span>
          <span class="material-symbols-outlined" v-else-if="sortKey.name===attr">arrow_drop_down</span>
        </div>
      </th>
      <th v-if="removable" class="red fw-semibold">
        Remove
      </th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="item of sortedItems" :key="item.id" :class="{'activeTableRow': current === item, 'hoverTableRow': clickableRows}">
      <td v-for="attr of attributes" :key="attr" :class="{'clickable': clickableRows}" @click="emitRowClickEvent(item)">
        {{ displayAttributeForItem(item, attr) }}
      </td>
      <td v-if="removable" class="clickable" @click="emitRowRemoveEvent(item)">
        <span class="material-symbols-outlined d-flex justify-content-center red">delete</span>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script>
// @author leon
export default {
  name: "TableArrayComponent",
  data() {
    return {
      sortKey: null,
      sortAsc: true,
      current: null,
    };
  },
  props: {
    items: Array,
    attributes: Object,
    sortable: Boolean,
    clickableRows: Boolean,
    removable: Boolean,
  },
  created() {
    if (this.sortable && this.attributes) {
      this.sortKey = this.attributes[Object.keys(this.attributes)[0]];
    }
  },
  computed: {
    sortedItems() {
      let sorted = [...this.items];
      if (this.sortKey && this.sortable) {
        sorted.sort((a, b) => {
          return this.displayAttributeForItem(b, this.sortKey).localeCompare(this.displayAttributeForItem(a, this.sortKey));
        });
        if (this.sortAsc) {
          sorted.reverse();
        }
      }
      return sorted;
    },
  },
  methods: {
    sort(key) {
      if (!this.sortable) {
        return;
      }

      if (key === this.sortKey.name) {
        this.sortAsc = !this.sortAsc;
      } else {
        this.sortKey = this.attributes[key];
        this.sortAsc = true;
      }
    },
    capitalizeText(text) {
      if (!text) {
        return text;
      }
      return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
    },
    emitRowClickEvent(item) {
      if (this.clickableRows && item) {
        this.current = this.current !== item ? item : null;
        this.$emit('row-click', item);
      }
    },
    emitRowRemoveEvent(item) {
      if (this.removable && item) {
        this.$emit('row-remove', item);
      }
    },
    displayAttributeForItem(item, attribute) {
      let value = item[attribute.name];
      return `${value ? attribute(value) : null}`;
    }
  },
}
</script>

<style scoped>
.red {
  color: #a00;
}

.clickable {
  cursor: pointer;
}

table {
  border-collapse: collapse;
  margin: auto;
}

th, td {
  border: 1px solid #000;
  padding: 8px;
  width: 10vw;
  height: 5vh;
  text-align: center;
  justify-content: center;
}

thead {
  background-color: lightgray;
}

tr:nth-child(even) {
  background-color: #eee;
}
</style>