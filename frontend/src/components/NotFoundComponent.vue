<template>
  <div class="flex flex-col justify-center text-center m-4">
    <div>
      <TitleComponent :page-title="'Error 404'" />
    </div>
    <p class="m-4">Oops! We couldn't find that in our warehouse...</p>
    <div>
      <ButtonComponent button-text="Go back" @click="goBack" class="w-32 h-12" />
    </div>
    <img src="../../static/images/lost_warehouse.png" alt="thumb" class="mx-64 mt-10"/>
  </div>
</template>

<script>
import ButtonComponent from "@/components/general/SolarButton";
import TitleComponent from "@/components/general/SolarTitle";

export default {
  name: "NotFoundComponent",
  components: {
    ButtonComponent,
    TitleComponent,
  },
  methods: {
    goBack() {
      let previousUrl = this.$router?.options?.history?.state?.back;
      let url = previousUrl ?? "/login";
      this.$router.push(url);
    },
  },
}
</script>

<style scoped>

</style>