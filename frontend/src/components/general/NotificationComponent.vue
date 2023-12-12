<template>
  <div class="notificationContainer"></div>
</template>


<script>
export default {
  name: "NotificationComponent",

  methods: {
    createSuccessfulNotification(message) {
      const notificationContainer = document.querySelector('.notificationContainer');

      const notification = document.createElement('div');
      notification.classList.add('notification');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const checkmarkIcon = document.createElement('span');
      checkmarkIcon.classList.add('material-symbols-outlined', 'checkmark');
      checkmarkIcon.textContent = 'check_circle';

      icon.appendChild(checkmarkIcon);

      notification.appendChild(notificationMessage);
      notification.appendChild(icon);
      notificationContainer.appendChild(notification);

      setTimeout(() => {
        notification.remove();
      }, 4000);
    },
    createUnsuccessfulNotification(message) {
      const notificationContainer = document.querySelector('.notificationContainer');

      const notification = document.createElement('div');
      notification.classList.add('notification');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const warningIcon = document.createElement('span');
      warningIcon.classList.add('material-symbols-outlined', 'warning');
      warningIcon.textContent = 'report';

      icon.appendChild(warningIcon);

      notification.appendChild(notificationMessage);
      notification.appendChild(icon);
      notificationContainer.appendChild(notification);

      setTimeout(() => {
        notification.remove();
      }, 4000);
    },
  },

}
</script>

<style>

.notificationContainer {
  position: absolute;
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 1rem;
  height: auto;
  width: auto;
  background: none;
  //border: 1px solid #222;
  right: 5%;
  bottom: 5%;
}

.notification {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 1rem;
  gap: 1rem;
  padding: 1rem;
  height: 50px;
  background: #fff;
  border-radius: 5px;
  animation: fade 4s ease-in-out;
  box-shadow: rgb(34, 34, 34, 0.2) 0 0 2px 1px;
}

@keyframes fade {

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
    margin-bottom: 15px;
    opacity: 0;
  }

}

.message {
  font-size: 1em;
  font-weight: 500;
  color: #222;
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.material-symbols-outlined.checkmark {
  color: #39a65e;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

.material-symbols-outlined.warning {
  color: #ce4744;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

</style>