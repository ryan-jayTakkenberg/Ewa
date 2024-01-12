<template>
  <div class="main">

    <div class="backgroundImgContainer"></div>

    <div class="loginContainer">

      <div class="logoContainer"></div>
      <div class="welcomeContainer" v-show="!showLoginForm && !showPasswordResetForm">
        <h1 class="title">Welcome</h1>
        <p class="description">This is a management system regarding Solar Sedum, an innovative and sustainable solar panel company.</p>
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

          <button type="submit" class="loginButton">Login</button>
        </form>

<!--      &lt;!&ndash; Password Reset Form &ndash;&gt;-->
<!--      <div class="passwordResetContainer" v-show="showPasswordResetForm">-->
<!--        <div class="headerWrapper">-->
<!--          <div class="crossWrapper" v-on:click="togglePasswordResetForm"><span class="material-symbols-outlined">close</span></div>-->
<!--        </div>-->

<!--        <form v-on:submit.prevent="submitPasswordReset">-->
<!--          <div class="inputWrapper">-->
<!--            <input type="email" name="email" v-model="emailInput" required>-->
<!--            <label for="email">Email</label>-->
<!--          </div>-->

<!--          &lt;!&ndash; Reset Password Button Container &ndash;&gt;-->
<!--          <div class="resetPasswordButtonContainer">-->
<!--            <button class="loginButton">Reset Password</button>-->
<!--          </div>-->
<!--        </form>-->
<!--       </div><div class="linkWrapper" v-show="!showPasswordResetForm">-->
<!--      <a href="#" v-on:click.prevent="togglePasswordResetForm">Can't sign in?</a>-->
<!--        </div>-->
<!--      &lt;!&ndash; Link to Toggle Password Reset Form &ndash;&gt;-->

      </div>

    </div>

</template>

<script>

import {isAdmin, setAdmin, setId, setJWT, setUsername} from "@/data";
import {postAPI} from "@/backend";

export default {
  name: 'LoginComponent',

  data() {
    return {
      showLoginForm: false,
      emailInput: '',
      passwordInput: '',
      errorMessage: '',
      showPasswordResetForm: false,
      resetErrorMessage: '',
      newPasswordInput: '',
      resetToken:'',
    }
  },

  methods: {

    //TODO set in own component for M&T
    // async submitNewPassword() {
    //   try {
    //     let response = await postAPI("/api/reset-password", {
    //       token: this.resetToken,
    //       newPassword: this.newPasswordInput
    //     });
    //
    //     // Handle the response
    //     console.log("Password reset successful", response);
    //     this.newPasswordInput = '';
    //     this.showNewPasswordForm = false;
    //     // todo validation
    //   } catch (error) {
    //     console.error("Error in setting new password", error);
    //     // todo validation
    //   }
    // },
    //
    // async submitPasswordReset() {
    //
    //   try {
    //     let response = await postAPI("/api/request-password-reset?email=" + encodeURIComponent(this.emailInput));
    //     console.log("Reset email sent", response);
    //     this.emailInput = '';
    //     this.togglePasswordResetForm();
    //   } catch (error) {
    //     console.error("Error sending reset email", error);
    //     this.resetErrorMessage = 'Error sending reset email. Please try again.';
    //   }
    // },

    showLogin() {
      this.showLoginForm = true;
      this.showPasswordResetForm = false;
    },

    hideLogin() {
      this.errorMessage = '';
      this.emailInput = '';
      this.passwordInput = '';
      this.showLoginForm = false;
    },

    async submitForm() {
      let account = await postAPI("/api/authentication/login", {
        email: this.emailInput,
        password: this.passwordInput,
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).catch(() => null);

      let jwt = account?.headers?.authorization;
      if (!jwt) {
        this.errorMessage = 'Invalid login, please try again';
        return;
      }

      setJWT(jwt);
      setAdmin(account.data.permissionLevel === "ADMIN");
      setId(account.data.id);
      setUsername(account.data.name);
      console.log('Logged in user: ', account.data);

      try {
        this.$router.push(isAdmin() ? '/admin-overview' : '/viewer-overview');
      } catch (error) {
        console.error(error);
      }
    },

    // togglePasswordResetForm() {
    //   this.showPasswordResetForm = !this.showPasswordResetForm;
    //   this.showLoginForm = false;
    //   this.errorMessage = '';
    // },

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
  background: var(--col-white-100)
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
  font-size: var(--font-size-large);
  font-weight: var(--font-weight-fat);
  color: var(--col-black);
}

.description {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--col-white-500);
}

.showLoginFormButton {
  padding: 1.5em 1em;
  border-radius: 5px;
  text-transform: uppercase;
  font-size: 1em;
  font-weight: var(--font-weight-bold);
  background: transparent;
  border: 2px solid var(--col-black);;
  color: var(--col-black);
  margin-top: 2rem;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

form {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1rem;
  border: 2px solid var(--col-black);
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
  background: var(--col-white-150);
  border-radius: 3px;
  font-size: 0.85em;
  font-weight: var(--font-weight-fat);
  color: var(--col-black);
  z-index: 1;
}

form label {
  position: absolute;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-fat);
  color: var(--col-white-700);
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
  outline: 2px solid var(--col-black);
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
    font-size: 10px;
    top: -0.25em;
    padding-left: 0.75em;
    transform: translateY(25%);
    opacity: 0;
  }
  100% {
    font-size: 10px;
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
  font-weight: var(--font-weight-bold);
  background: transparent;
  border: 2px solid var(--col-black);
  color: var(--col-black);
  margin-top: 2rem;
  cursor: pointer;
  transition: 0.2s ease-in-out;
}

.showLoginFormButton:hover,
.loginButton:hover {
  background: var(--col-black);
  color: var(--col-white-100);
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
  background: var(--col-white-150);
}

.errorMessageWrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 5px;
  border: 2px solid var(--col-error);
  padding: 0.5rem 1rem;
  margin-top: 1rem;
}

.errorMessage {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
}

a {
  font-size: var(--font-size-small);
  font-weight: var(--font-weight-bold);
  color: var(--col-black);
}

a:hover {
  text-decoration: underline;
}

.material-symbols-outlined {
   font-size: var(--font-size-large);
   font-variation-settings:
       'FILL' 1,
       'wght' 600,
       'GRAD' 0,
       'opsz' 48
}

.report-icon {
  color: var(--col-error);
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

</style>
