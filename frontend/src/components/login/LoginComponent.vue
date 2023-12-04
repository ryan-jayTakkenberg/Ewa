<template>
  <div class="main">
    <div class="backgroundImgContainer">

    </div>
    <div class="loginContainer">
      <div class="logoContainer"></div>
      <div class="welcomeContainer" v-show="!showLoginForm && !showPasswordResetForm">
        <h1 class="title">Welcome</h1>
        <p class="description">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aperiam delectus deleniti dolorem eius fuga hic iste iure minus.</p>
        <button class="showLoginFormButton" v-on:click="showLogin()">Login</button>
      </div>

        <form v-on:submit.prevent="submitForm" v-show="showLoginForm">
          <div class="headerWrapper">
            <div class="crossWrapper" v-on:click="hideLogin"><span class="material-symbols-outlined">close</span></div>
          </div>

          <div class="inputContainer">
            <div class="inputWrapper">
              <input type="text" name="email" v-model="emailInput" required>
              <label for="email">Email</label>
            </div>

            <div class="inputWrapper">
              <input type="password" name="password" v-model="passwordInput" required>
              <label for="password">Password</label>
            </div>
          </div>

            <div class="errorMessageWrapper" v-show="errorMessage">
              <p class="errorMessage"> {{ errorMessage }} </p>
              <span class="material-symbols-outlined report-icon">report</span>
            </div>

          <button class="loginButton">Login</button>
        </form>

      <!-- Password Reset Form -->
      <div class="passwordResetContainer" v-show="showPasswordResetForm">
        <div class="headerWrapper">
          <div class="crossWrapper" v-on:click="togglePasswordResetForm"><span class="material-symbols-outlined">close</span></div>
        </div>

        <form v-on:submit.prevent="submitPasswordReset">
          <div class="inputWrapper">
            <input type="email" name="email" v-model="emailInput" required>
            <label for="email">Email</label>
          </div>

          <!-- Reset Password Button Container -->
          <div class="resetPasswordButtonContainer">
            <button class="loginButton">Reset Password</button>
          </div>
        </form>
      </div>


      <!-- Link to Toggle Password Reset Form -->
      <div class="linkWrapper" v-show="!showPasswordResetForm">
        <a href="#" v-on:click.prevent="togglePasswordResetForm">Can't sign in?</a>
      </div>

    </div>
  </div>

</template>

<script>

import {setAdmin, setId, setJWT, setUsername} from "@/data";
import Team from "@/models/team";
import User from "@/models/user";
import Product from "@/models/product";
import Project from "@/models/project";
import Warehouse from "@/models/warehouse";
import {postAPI} from "@/backend";

export default {
  name: 'LoginComponent',

  data() {
    return {
      showLoginForm: false,
      usernameDummy: 'test',
      passwordDummy: 'test',
      emailInput: '',
      passwordInput: '',
      errorMessage: '',
      showPasswordResetForm: false,
      resetErrorMessage: ''

    }
  },

  methods: {

    async submitPasswordReset() {
      try {
        let response = await postAPI("/api/request-password-reset?email=" + encodeURIComponent(this.emailInput));

        // Handle the response
        console.log("Reset email sent", response);
        this.emailInput = '';
        this.togglePasswordResetForm();
        // Add any user feedback message here
      } catch (error) {
        console.error("Error sending reset email", error);
        this.resetErrorMessage = 'Error sending reset email. Please try again.';
      }
    },
    showLogin() {

      this.showLoginForm = true;
      this.showPasswordResetForm = false;
    },

    hideLogin() {

      this.errorMessage = '';
      this.usernameInput = '';
      this.passwordInput = '';
      this.showLoginForm = false;
    },

    async submitForm() {
      let response = await postAPI("/api/authentication/login", {
        email: this.emailInput,
        password: this.passwordInput,
      });

      let jwt = response?.headers?.authorization;
      if (!jwt) {
        this.errorMessage = 'Invalid Email or Password';
        return;
      }

      setJWT(jwt);
      setAdmin(response.data.permissionLevel === "ADMIN");
      setId(response.data.id);
      setUsername(response.data.name);
      // setUserTeam(response.data.team);

      console.log(response.data);

      try {
        const [teams, users, products, projects, warehouses] = await Promise.all([
          Team.getDatabase(),
          User.getDatabase(),
          Product.getDatabase(),
          Project.getDatabase(),
          Warehouse.getDatabase(),
        ]);

        Team.teams = teams;
        User.users = users;
        Product.products = products;
        Project.projects = projects;
        Warehouse.warehouses = warehouses;

        // Check the user's permission level and push the corresponding route
        if (response.data.permissionLevel === "ADMIN") {
          this.$router.push('/admin-overview');
        } else {
          this.$router.push('/viewer-overview');
        }

      } catch (error) {
        console.error(error);
      }
    },
    togglePasswordResetForm() {
      this.showPasswordResetForm = !this.showPasswordResetForm;
      this.showLoginForm = false;
      this.errorMessage = '';
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
  background: url("../../../static/images/loginBackground.jpg") center no-repeat;
  background-size: cover;
}

.loginContainer {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100svh;
  width: 25%;
  padding: 0 4rem;
  background: #ffffff;
}

.logoContainer {
  position: absolute;
  top: 5%;
  height: 125px;
  width: 200px;
  background: url("../../../static/images/solar_sedum_logo.svg") center no-repeat;
  background-size: contain;
}

.welcomeContainer {
  display: flex;
  flex-direction: column;
  height: auto;
  width: 100%;
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
  text-align: left;
  font-size: 30px;
  font-weight: 700;
  color: #222;
}

.description {
  font-size: 12px;
  font-weight: 400;
  color: #999;
}

.showLoginFormButton {
  padding: 1.5em 1em;
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

form {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1rem;
  border: 2px solid #222;
  border-radius: 5px;
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

form .inputContainer {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

form .inputWrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 4em;
  border-radius: 3px;
}

form .input-value + * {
  clear: both;
}

form input {
  position: absolute;
  height: 100%;
  width: 100%;
  padding: 1.35em 0.65em 0 0.65em;
  border: none;
  outline: none;
  background: #f5f5f5;
  border-radius: 3px;
  font-size: 0.85em;
  font-weight: 700;
  color: #222;
  z-index: 1;
}

form label {
  position: absolute;
  font-size: 0.75em;
  font-weight: 700;
  color: #747474;
  text-transform: uppercase;
  padding-left: 1rem;
  z-index: 2;
  pointer-events: none;
  animation: labelIn;
  animation-duration: 0.2s;
  animation-direction: reverse;
  animation-fill-mode: forwards;
}

form input:focus {
  outline: 2px solid #222;
  background: none;
}

form input:focus + label, form input:valid + label {
  animation: labelOut;
  animation-duration: 0.2s;
  animation-direction: normal;
  animation-fill-mode: forwards;
}

@keyframes labelIn {

  0% {
    opacity: 1;
    transform: translateY(0);
  }

  100% {
    opacity: 0;
    transform: translateY(-25%);
  }

}

@keyframes labelOut {

  0% {
    font-size: 0.6em;
    top: -0.25em;
    padding-left: 0.75em;
    transform: translateY(25%);
    opacity: 0;
  }

  100% {
    font-size: 0.6em;
    top: -0.25em;
    padding-left: 0.75em;
    transform: translateY(0);
    opacity: 1;
  }

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

.showLoginFormButton:hover,
.loginButton:hover {
  background: #222;
  color: #fff;
}

.headerWrapper {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: right;
  height: 50px;
  margin-bottom: 1rem;
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
  background: #f5f5f5;
}

.errorMessageWrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 5px;
  border: 2px solid red;
  padding: 0.5rem 1rem;
  margin-top: 1rem;
}

.errorMessage {
  font-size: 0.8em;
  font-weight: 600;
}

.linkWrapper {
  position: absolute;
  bottom: 5%;
  display: flex;
  align-items: center;
}

a {
  font-size: 15px;
  font-weight: 500;
  color: #222;
}

a:hover {
  text-decoration: underline;
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

  .loginContainer {
    padding: 0 3rem;
    width: 30%;
  }

}

@media screen and (max-width: 1100px) {

  .loginContainer {
    padding: 0 3rem;
    width: 40%;
  }

}

@media screen and (max-width: 950px) {

  .backgroundImgContainer {
    display: none;
  }

  .loginContainer {
    padding: 0 10rem;
    width: 100%;
  }

  .logoContainer {
    height: 250px;
    width: 300px;
  }

  .title {
    font-size: 40px;
  }

  .description {
    font-size: 15px;
  }

  .showLoginFormButton {
    margin-top: 3rem;
  }

}

@media screen and (max-width: 750px) {

  .loginContainer {
    padding: 0 7.5rem;
    width: 100%;
  }

}

@media screen and (max-width: 600px) {

  .backgroundImgContainer {
    display: none;
  }

  .loginContainer {
    padding: 0 5rem;
    width: 100%;
  }

  .logoContainer {
    height: 125px;
    width: 200px;
  }

  .errorMessage {
    font-size: 0.65em;
  }

  .title {
    font-size: 30px;
  }

  .description {
    font-size: 12px;
  }

}

@media screen and (max-width: 400px) {

  .loginContainer {
    padding: 0 2rem;
    width: 100%;
  }

}
.passwordResetContainer {
  width: 100%;
  padding: 0.5rem;
  border: 2px solid #222;
  border-radius: 5px;
}



</style>
