<template>
  <div></div>
</template>

<script>
export default {
  name: "NotificationComponent",
  methods: {
    createSuccessfulNotification(message) {
      const notificationContainer = document.createElement('div');
      notificationContainer.classList.add('notificationMessageContainer');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const checkmarkIcon = document.createElement('span');
      checkmarkIcon.classList.add('material-symbols-outlined', 'checkmark');
      checkmarkIcon.textContent = 'check_circle';

      icon.appendChild(checkmarkIcon);

      notificationContainer.appendChild(notificationMessage);
      notificationContainer.appendChild(icon);

      document.body.appendChild(notificationContainer);

      setTimeout(() => {
        notificationContainer.remove();
      }, 5000);
    },
    createUnsuccessfulNotification(message) {
      const notificationContainer = document.createElement('div');
      notificationContainer.classList.add('notificationContainer');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const warningIcon = document.createElement('span');
      warningIcon.classList.add('material-symbols-outlined', 'warning');
      warningIcon.textContent = 'report';

      icon.appendChild(warningIcon);

      notificationContainer.appendChild(notificationMessage);
      notificationContainer.appendChild(icon);

      document.body.appendChild(notificationContainer);

      setTimeout(() => {
        notificationContainer.remove();
      }, 5000);
    },
  },
  staticMethods: {
    createSuccessfulNotification(message) {
      const notificationContainer = document.createElement('div');
      notificationContainer.classList.add('notificationMessageContainer');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const checkmarkIcon = document.createElement('span');
      checkmarkIcon.classList.add('material-symbols-outlined', 'checkmark');
      checkmarkIcon.textContent = 'check_circle';

      icon.appendChild(checkmarkIcon);

      notificationContainer.appendChild(notificationMessage);
      notificationContainer.appendChild(icon);

      document.body.appendChild(notificationContainer);

      setTimeout(() => {
        notificationContainer.remove();
      }, 5000);
    },
    createUnsuccessfulNotification(message) {
      const notificationContainer = document.createElement('div');
      notificationContainer.classList.add('notificationContainer');

      const notificationMessage = document.createElement('div');
      notificationMessage.classList.add('message');
      notificationMessage.textContent = message;

      const icon = document.createElement('div');
      icon.classList.add('icon');
      const warningIcon = document.createElement('span');
      warningIcon.classList.add('material-symbols-outlined', 'warning');
      warningIcon.textContent = 'report';

      icon.appendChild(warningIcon);

      notificationContainer.appendChild(notificationMessage);
      notificationContainer.appendChild(icon);

      document.body.appendChild(notificationContainer);

      setTimeout(() => {
        notificationContainer.remove();
      }, 5000);
    }
  }
}
</script>

<style>

.notificationContainer {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 2rem 1rem;
  height: 50px;
  background: #fff;
  border-radius: 5px;
  right: 5%;
  bottom: 5%;
  animation: fade 5.1s ease-in-out;
  box-shadow: rgba(0, 0, 0, 0.15) 0 0 10px 1px;
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
  font-weight: 600;
  color: #222;
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.material-symbols-outlined.checkmark {
  transform: scale(1.2);
  color: #39a65e;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

.material-symbols-outlined.warning {
  transform: scale(1.2);
  color: #ce4744;
  font-variation-settings:
      'FILL' 0,
      'wght' 400,
      'GRAD' 0,
      'opsz' 24
}

</style>