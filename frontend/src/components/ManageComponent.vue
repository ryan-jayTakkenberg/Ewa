<template>
  <NavBarComponent></NavBarComponent>
  <div>
    <div class="nav-box">
      <div @click="goToPage('product')" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">inventory_2</span></div>
        <div class="content-box-bottom product"><h3 class="subtitle">Product</h3></div>
      </div>
      <div @click="goToPage('warehouse')" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">warehouse</span></div>
        <div class="content-box-bottom warehouse"><h3 class="subtitle">Warehouse</h3></div>
      </div>
      <div @click="goToPage('project')" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">tactic</span></div>
        <div class="content-box-bottom project"><h3 class="subtitle">Project</h3></div>
      </div>
      <div @click="goToPage('team')" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">folder_shared</span></div>
        <div class="content-box-bottom team"><h3 class="subtitle">Team</h3></div>
      </div>
      <div @click="goToPage('user')" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">person</span></div>
        <div class="content-box-bottom user"><h3 class="subtitle">User</h3></div>
      </div>
    </div>
    <div class="line rounded mt-3 mb-3"></div>
    <div>
      <router-view id="scrollTo" />
    </div>
  </div>
</template>

<script>
import NavBarComponent from "@/components/NavBarComponent";

export default {
  name: 'ManageComponent',
  components: {
    NavBarComponent,
  },
  methods: {
    goToPage(page) {
      let path = '/manage/' + page;
      if (this.$route.path === path) {
        this.$router.push('/manage');
        return;
      }

      this.$router.push(path);
      setTimeout(() => {
        let y = document.getElementById("scrollTo")?.offsetTop;
        if (y > window.screen.height / 2) {
          window.scrollTo({top: y, behavior: "smooth"});
        }
      }, 1);
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.subtitle {
  text-align: center;
  font-size: max(1.5vw, 15px);
}

.line {
  background-color: lightgray;
  height: 0.5vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

.nav-box{
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  height: fit-content;
  justify-content: center;
  align-content: center;
}

.content-box {
  height: max(10vw, 100px);
  width: max(15vw, 150px);
  border: 3px solid lightgray;
  border-radius: 10px;
  margin: 1vw;
  flex-shrink: 0;
}

.content-box:hover{
  border: 3px solid black;
  cursor: pointer;
}

.content-box-top{
  height: 70%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-box-bottom{
  border-top: 3px solid lightgray;
  padding: 5px;
  height: 30%;
  border-radius: 0 0 7px 7px;
}

.material-symbols-outlined{
  font-size: max(4vw, 40px);
  color: black;
}

.product{
  background: #F8FB81;
}

.warehouse{
  background: #ADE6F9;
}

.project{
  background: #FF9393;
}

.team{
  background: #5DDB88;
}

.user{
  background: #E8AA4E;
}
</style>
