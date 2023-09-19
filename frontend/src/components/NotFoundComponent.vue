<template>
  <div class="center">
    <h1>404 Not Found</h1>
    <router-link to="/" class="button">Login Page</router-link>
  </div>
</template>

<script>
export default {
  name: "NotFoundComponent"
}
</script>

<style scoped>
.center {
  margin: 1em 1em;
  text-align: center;
}

.button {
  background-color: gray;
  color: white;
  padding: 1em 1em;
  text-align: center;
  display: inline-block;
  font-size: 1em;
  border-radius: 1em;
}
</style>