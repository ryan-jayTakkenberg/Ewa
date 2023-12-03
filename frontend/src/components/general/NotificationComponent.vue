<template>
  <div></div>
</template>

<script>
export default {

  /* Add as child to your component and call function using
  this.$refs.notificationComponent.createNotification(message);
  Send a message as parameter */

  name: "NotificationComponent",

  methods: {
    createNotification(message) {

      const notificationContainer = document.createElement('div');
      notificationContainer.classList.add('successfulMessageContainer');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('successfulMessage');
      notificationMessage.textContent = message;

      const checkmark = document.createElement('div');
      checkmark.classList.add('checkmark');
      const checkmarkIcon = document.createElement('span');
      checkmarkIcon.classList.add('material-symbols-outlined', 'checkmark');
      checkmarkIcon.textContent = 'check_circle';

      checkmark.appendChild(checkmarkIcon);

      notificationContainer.appendChild(notificationMessage);
      notificationContainer.appendChild(checkmark);

      document.body.appendChild(notificationContainer);

      setTimeout(() => {
        notificationContainer.remove();
      }, 5000);
    },
  },

}
</script>

<style>

.successfulMessageContainer {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
  padding: 2rem 1rem;
  height: 50px;
  background: #fff;
  border-radius: 5px;
  right: 5%;
  bottom: 5%;
  animation: successful 5.1s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.15) 0 0 10px 1px;
}

@keyframes successful {

  0% {
    margin-bottom: -15px;
    opacity: 0;
  }

  7% {
    margin-bottom: 0;
    opacity: 1;
  }

  90% {
    margin-bottom: 0;
    opacity: 1;
  }

  100% {
    margin-bottom: 20px;
    opacity: 0;
  }

}

.successfulMessage {
  font-size: 1em;
  font-weight: 600;
  color: #222;
}

.checkmark {
  display: flex;
  align-items: center;
  justify-content: center;
}

.material-symbols-outlined.checkmark {
  transform: scale(1.1);
  color: #4fd97f;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

</style>