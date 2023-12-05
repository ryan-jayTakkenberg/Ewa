<template>
  <div class="main">
    <div class="backgroundImgContainer"></div>
    <div class="loginContainer">
      <div class="logoContainer"></div>
      <div class="resetPasswordContainer">
        <h1 class="title">Reset Your Password</h1>
        <form v-on:submit.prevent="submitNewPassword" class="resetPasswordForm">
          <div class="inputWrapper">
            <input type="password" name="newPassword" v-model="newPasswordInput" required>
            <label for="newPassword">New Password</label>
          </div>
          <button class="loginButton">Set New Password</button>
        </form>
        <div class="errorMessageWrapper" v-show="errorMessage">
          <p class="errorMessage">{{ errorMessage }}</p>
          <span class="material-symbols-outlined report-icon">report</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { postAPI } from "@/backend";

  export default {
  name: 'PasswordResetComponent',
  data() {
  return {
  newPasswordInput: '',
  errorMessage: '',
  resetToken: ''
};
},
  methods: {
    async submitNewPassword() {
      if (!this.resetToken) {
        this.errorMessage = 'Invalid or missing token.';
        return;
      }

      if (!this.newPasswordInput) {
        this.errorMessage = 'Please enter a new password.';
        return;
      }

      try {
        const params = new URLSearchParams();
        params.append('token', this.resetToken);
        params.append('newPassword', this.newPasswordInput);

        let response = await postAPI("/api/reset-password?" + params.toString());

        // Handle success response
        console.log("Password reset successful", response);
        alert("Password has been reset successfully. You will be redirected to the login page.");
        this.$router.push('/login');
      } catch (error) {
        console.error("Error resetting password", error);
        this.errorMessage = 'Error resetting password. Please try again.';
      }
    }


  },
  mounted() {
  this.resetToken = this.$route.query.token;
}
};
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
}.resetPasswordContainer {
   display: flex;
   flex-direction: column;
   width: 100%;
   animation: fadeWelcome 0.5s ease-in-out;
 }

.title {
  text-align: center;
  font-size: 30px;
  font-weight: 700;
  color: #222;
  margin-bottom: 2rem;
}

.resetPasswordForm {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 1rem;
  border: 2px solid #222;
  border-radius: 5px;
  animation: fadeForm 0.3s ease-in-out;
}

.inputWrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 4em;
  border-radius: 3px;
  margin-bottom: 2rem;
}
input {
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

label {
  position: absolute;
  font-size: 0.75em;
  font-weight: 700;
  color: #747474;
  text-transform: uppercase;
  padding-left: 1rem;
  z-index: 2;
  pointer-events: none;
  transition: all 0.2s ease;
}

input:focus {
  outline: 2px solid #222;
  background: none;
}

input:focus + label, input:valid + label {
  top: -0.75em;
  font-size: 0.6em;
  padding-left: 0.75em;
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

.loginButton:hover {
  background: #222;
  color: #fff;
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
  color: red;
}

.report-icon {
  color: red;
  font-size: 30px;
  font-variation-settings: 'FILL' 1, 'wght' 600, 'GRAD' 0, 'opsz' 48;
}


</style>
