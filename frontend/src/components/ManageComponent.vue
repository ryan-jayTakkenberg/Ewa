<template>
<!--  <nav class="navbar navbar-light bg-light">-->
<!--    <div class="empty"></div>-->
<!--    <div class="container rounded-3" @click="goToProductDetail">-->
<!--      <div class="logoContainer">-->
<!--        <span class="material-symbols-outlined">inventory_2</span>-->
<!--      </div>-->
<!--      <div class="textContainer rounded-bottom-3" style="background-color:aqua;">-->
<!--        <span class="fw-semibold">Product</span>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="container rounded-3" @click="goToWarehouseDetail">-->
<!--      <div class="logoContainer">-->
<!--        <span class="material-symbols-outlined">warehouse</span>-->
<!--      </div>-->
<!--      <div class="textContainer rounded-bottom-3" style="background-color:yellow;">-->
<!--        <span class="fw-semibold">Warehouses</span>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="container rounded-3" @click="goToProjectDetail">-->
<!--      <div class="logoContainer">-->
<!--        <span class="material-symbols-outlined">tactic</span>-->
<!--      </div>-->
<!--      <div class="textContainer rounded-bottom-3" style="background-color:orangered;">-->
<!--        <span class="fw-semibold">Projects</span>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="container rounded-3">-->
<!--      <div class="logoContainer">-->
<!--        <span class="material-symbols-outlined">folder_shared</span>-->
<!--      </div>-->
<!--      <div class="textContainer rounded-bottom-3" style="background-color:limegreen;">-->
<!--        <span class="fw-semibold">Teams</span>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="container rounded-3">-->
<!--      <div class="logoContainer">-->
<!--        <span class="material-symbols-outlined">person</span>-->
<!--      </div>-->
<!--      <div class="textContainer rounded-bottom-3" style="background-color:orange;">-->
<!--        <span class="fw-semibold">Accounts</span>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="empty"></div>-->
<!--  </nav>-->
<!--<div>-->
<!--  <router-view>-->
<!--  </router-view>-->
<!--</div>-->
  <div>
    <div class="nav-box">
      <div @click="goToProductDetail" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">inventory_2</span></div>
        <div class="content-box-bottom product"><h3>Product</h3></div>
      </div>
      <div @click="goToWarehouseDetail" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">warehouse</span></div>
        <div class="content-box-bottom warehouse"><h3>Warehouse</h3></div>
      </div>
      <div @click="goToProjectDetail" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">tactic</span></div>
        <div class="content-box-bottom project"><h3>Project</h3></div>
      </div>
      <div @click="goToTeamDetail" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">folder_shared</span></div>
        <div class="content-box-bottom team"><h3>Team</h3></div>
      </div>
      <div @click="goToUserDetail" class="content-box">
        <div class="content-box-top"><span class="material-symbols-outlined">person</span></div>
        <div class="content-box-bottom user"><h3>User</h3></div>
      </div>
    </div>
    <div>
      <router-view>
      </router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ManageComponent',
  methods: {
    goToProductDetail(){
      this.$router.push('/manage/product')
    },
    goToProjectDetail(){
      this.$router.push('/manage/project')
    },
    goToWarehouseDetail(){
      this.$router.push('/manage/warehouse')
    },
    goToTeamDetail(){
      this.$router.push('/manage/team')
    },
    goToUserDetail(){
      this.$router.push('/manage/user')
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/*.empty {*/
/*  width: 0;*/
/*}*/

/*.container {*/
/*  display: flex;*/
/*  flex-direction: column;*/
/*  height: 100px;*/
/*  width: 150px;*/
/*  border: 2px solid black;*/
/*  margin: 0;*/
/*  padding: 0;*/
/*  cursor: pointer;*/
/*}*/

/*.logoContainer {*/
/*  display: flex;*/
/*  align-items: center;*/
/*  justify-content: center;*/
/*  height: 70%;*/
/*}*/

/*.textContainer {*/
/*  display: flex;*/
/*  align-items: center;*/
/*  justify-content: center;*/
/*  height: 30%;*/
/*  width: 100%;*/
/*  border-top: 2px solid black;*/
/*}*/

/*.material-symbols-outlined {*/
/*  font-size: 2em;*/
/*  font-variation-settings:*/
/*      'FILL' 0,*/
/*      'wght' 300,*/
/*      'GRAD' 0,*/
/*      'opsz' 24*/
/*}*/

.nav-box{
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  height: fit-content;
  justify-content: center;
  align-content: center;
}

.content-box{
  height: 150px;
  width: 200px;
  border: 3px solid lightgray;
  border-radius: 10px;
  margin: 15px;
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
  text-align: center;
  border-radius: 0 0 7px 7px;
}

.material-symbols-outlined{
  font-size: 70px;
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
