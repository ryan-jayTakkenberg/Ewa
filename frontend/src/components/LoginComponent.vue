<template>
  <div class="main">
    <div class="backgroundImgContainer">

    </div>
    <div class="container">
      <div class="logoContainer"></div>
      <div class="welcomeContainer">
        <div class="title">Welcome</div>
        <div class="description">This is Solar Sedum's management system. This system keeps track of the inventory. Please login as admin or user.</div>
      </div>

      <div class="loginContainer">
        <div class="buttonContainer">
          <button class="adminButton" v-on:click="showLogin('Admin')">Login as admin</button>
          <button class="userButton" v-on:click="showLogin('User')">Login as user</button>
        </div>

        <form v-on:submit.prevent="submitForm">
          <div class="headerWrapper">
            <div class="typeOfUser"><span class="material-symbols-outlined">person</span> {{ typeOfUser }}</div>
            <div class="crossWrapper" v-on:click="hideLogin"><span class="material-symbols-outlined">close</span></div>
          </div>
          <div class="inputWrapper">
            <label>Username</label>
            <input type="text" v-model="usernameInput" placeholder="username">
            <label>Password</label>
            <input type="password" v-model="passwordInput" placeholder="password">
            <div class="errorMessageWrapper" v-show="errorMessage">
              <div class="errorMessage"> {{ errorMessage }} </div>
              <span class="material-symbols-outlined report-icon">report</span>
            </div>
          </div>
          <button class="loginButton">Login</button>
        </form>

      </div>

      <div class="information">Need more information? <a href="#">click here</a></div>

    </div>
  </div>



</template>

<script>
export default {
  name: 'LoginComponent',


  data() {
    return {
      typeOfUser: '',
      usernameDummy: 'ewaAdmin1',
      passwordDummy: 'ewaPW1',
      usernameInput: '',
      passwordInput: '',
      errorMessage: '',
    }
  },

  methods: {

    showLogin(type) {

      this.typeOfUser = type;

      this.$el.querySelector('form').style.display = 'flex';
      this.$el.querySelector('.buttonContainer').style.display = 'none';
      this.$el.querySelector('.welcomeContainer').style.display = 'none';
    },

    hideLogin() {

      this.errorMessage = '';
      this.usernameInput = '';
      this.passwordInput = '';
      this.$el.querySelector('form').style.display = 'none';
      this.$el.querySelector('.buttonContainer').style.display = 'flex';
      this.$el.querySelector('.welcomeContainer').style.display = 'flex';
    },

    submitForm() {

      if (this.usernameInput !== this.usernameDummy && this.passwordInput !== this.passwordDummy) {
        this.errorMessage = 'Invalid Username & Password'
        return false;
      }

      if (this.usernameInput !== this.usernameDummy) {
        this.errorMessage = 'Invalid Username'
        return false;

      } if (this.passwordInput !== this.passwordDummy) {
        this.errorMessage = 'Invalid Password'
        return false;

      } else {
        this.$el.querySelector('.errorMessageWrapper').style.display = 'none';
        alert('Successful Login')
      }
    },
  },
}
</script>

<style scoped>

.main {
  display: flex;
  height: 100svh;
  width: 100svw;
  overflow: hidden;
}

.backgroundImgContainer {
  height: 100svh;
  width: 75%;
  background: url("../../static/images/loginBackground.jpg") center no-repeat;
  background-size: cover;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100svh;
  width: 25%;
  padding: 2rem;
  background: #f5f5f5;
  border-left: 1px solid #e5e5e5;
}

.logoContainer {
  height: 125px;
  width: 200px;
  background: url("../../static/images/solar_sedum_logo.svg") center no-repeat;
  background-size: contain;
}

.welcomeContainer {
  display: flex;
  flex-direction: column;
  height: auto;
  width: 80%;
  margin-top: 5rem;
  animation: fadeWelcome 0.5s ease-in-out;
}

@keyframes fadeWelcome {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0px) ;
  }
}

.title {
  font-size: 30px;
  font-weight: 700;
  color: #222;
}

.description {
  font-size: 12px;
  font-weight: 400;
  color: #999;
}

.loginContainer {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: auto;
  width: 80%;
  margin-top: 3rem;
}

.loginContainer .buttonContainer {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 2rem;
  transition: 0.2s ease-in-out;
}

.loginContainer form {
  display: none;
  flex-direction: column;
  width: 100%;
  padding: 1rem;
  border: 3px solid #e5e5e5;
  border-radius: 10px;
  margin-top: 4rem;
  animation: fadeForm 0.3s ease-in-out;
}

@keyframes fadeForm {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}


form .inputWrapper {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

form input {
  border: none;
  padding: 0.5rem;
  border-radius: 5px;
  font-size: 15px;
  font-weight: 400;
  outline: 1px solid #ccc;
}

form input:focus {
  outline: 2px solid #222;
}

button {
  padding: 1.5em 1em;
  border-radius: 5px;
  text-transform: uppercase;
  font-size: 1em;
  font-weight: 600;
  background: transparent;
  border: 2px solid #222;
  color: #222;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

.loginButton {
  padding: 1em 0.5em;
  border-radius: 5px;
  text-transform: uppercase;
  font-size: 1em;
  font-weight: 600;
  background: transparent;
  border: 2px solid #222;
  color: #222;
  margin-top: 2rem;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

button:hover,
.loginButton:hover {
  background: #222;
  color: #fff;
}

label {
  font-size: 15px;
  font-weight: 500;
}

.headerWrapper {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  margin-bottom: 3rem;
}

.typeOfUser {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 15px;
  font-weight: 600;
  color: #222;
}

.crossWrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 5px;
  height: 50px;
  width: 50px;
  transition: 0.2s;
}

.crossWrapper:hover {
  background: #e5e5e5;
}

.errorMessageWrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 0.8em;
  font-weight: 500;
  border-radius: 5px;
  border: 2px solid red;
  padding: 0.5rem 1rem;
  margin-top: 1.5rem;
}

.information {
  display: flex;
  align-items: center;
  margin-top: 3rem;
  font-size: 12px;
  font-weight: 500;
  color: #222;
  gap: 5px;
}

.material-symbols-outlined {
   font-size: 30px;
   font-variation-settings:
       'FILL' 1,
       'wght' 600,
       'GRAD' 0,
       'opsz' 48
}

.report-icon {
  color: red;
}

@media screen and (max-width: 1500px) {
  .container {
    padding: 1rem;
    width: 30%;
  }

  button {
    font-size: 0.8em;
  }

  .loginButton {
    font-size: 12px;
  }
}

@media screen and (max-width: 1200px) {
  .container {
    padding: 0.8rem;
    width: 35%;
  }

  button {
    font-size: 0.7em;
  }

  .loginButton {
    font-size: 10px;
  }
}

</style>
