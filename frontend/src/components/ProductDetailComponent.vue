<template>
  <div class="box">
    <div class="content">
      <div class="content-header">
      </div>
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h3>Product information</h3>
            <label>Product ID*</label>
            <input type="text" placeholder="A12345">
            <label>Name*</label>
            <input type="text" placeholder="Solar panel">
            <label>Weight in kg*</label>
            <input type="text" placeholder="1,525">
            <label>Description</label>
            <textarea placeholder="Description . . ."></textarea>
          </form>
          <button>Add new product</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductManageComponent"
}
</script>

<style scoped>
.box{
  border-top: 2px solid lightgray;
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-content: center;
}

.content{
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 30px 0 30px 0;
}

.content-header{
  height: 175px;
  background: url("../../static/images/manageDetailHeader2.jpg") center no-repeat;
  background-size: cover;
  border-radius: 20px 20px 0 0 ;
}

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

label{
  width: 100%;
  margin: 10px 0 5px 0;
  font-weight: bold;
}

input{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid gray;
  width: 100%;
  appearance: none;
}

textarea{
  width: 100%;
  height: 125px;
  overflow: hidden;
  resize: none;
  padding: 5px;
  border-radius: 10px;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

@media (max-width: 500px) {
  .content{
    width: 70%;
  }
}
</style>