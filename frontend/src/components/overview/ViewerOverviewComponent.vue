<script>
export default {

  name: "UserOverviewComponent",

  data() {
    return {
      userName: 'Jason',
      meetingTime: '11:30 - 12:30',
      meetingLocation: 'Warehouse 2',
      }
    },

  computed: {
    dayOfTheWeek() {
      const today = new Date();
      today.setDate(today.getDate());
      const options = { weekday: 'short' };

      return today.toLocaleDateString(undefined, options);
    },
    numberOfTheDay() {
      const today = new Date();
      return today.getUTCDate()
    }
  },

}

</script>

<template>

  <!--- Persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="welcomeContainer">
      <h1>Hi {{ userName }}.</h1>
      <p class="description">This is an overview of information that is important to you.</p>
    </div>
    <div class="profilePicContainer">
      <div class="profilePic"></div>
    </div>

  </div>

  <!--- Agenda ---------------------------------------------------------------------------------->
  <div class="agendaContainer">

    <div class="dateContainer">
      <div class="date">
        <p class="dayOfTheWeek">{{ dayOfTheWeek }}</p>
        <p class="dayOfTheWeekNum">{{ numberOfTheDay }}</p>
      </div>
      <div class="insightContainer">
        <div class="meetingInsights">
          <span class="material-symbols-outlined">schedule</span>
          <p class="meetingText"> {{ meetingTime }} </p>
        </div>
        <div class="meetingInsights">
          <span class="material-symbols-outlined">location_on</span>
          <p class="meetingText"> {{ meetingLocation }} </p>
        </div>
      </div>
    </div>

  </div>

</template>

<style scoped>

.personaContainer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 2rem 3rem;
}

.welcomeContainer {
  display: flex;
  align-items: flex-start;
  flex-direction: column;
}

h1 {
  font-size: 2rem;
  font-weight: 700;
  color: #222;
}

.description {
  font-size: 1rem;
  font-weight: 400;
  color: #ccc;
}

.profilePicContainer {
  height: 100px;
  width: 100px;
  background: #f5f5f5;
  border-radius: 50%;
}

.agendaContainer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 2rem 3rem;
  border-bottom: 2px solid #e5e5e5;
}

.dateContainer {
  display: flex;
  width: 100%;
  height: 200px;
  border: 2px solid #e5e5e5;
  border-radius: 15px;
}

.date {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  height: 100%;
  width: 15%;
  border-right: 2px solid #e5e5e5;
}

.dayOfTheWeek {
  font-size: 2rem;
  font-weight: 700;
  line-height: 1;
  color: #222;
}

.dayOfTheWeekNum {
  font-size: 4rem;
  font-weight: 800;
  line-height: 1;
  color: #222;
}

.insightContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 1rem;
  height: 100%;
  width: 15%;
  padding: 2rem;
}

.meetingInsights {
  display: flex;
  align-items: center;
  justify-content: left;
  width: 100%;
}

.meetingText {
  margin-left: 1rem;
  font-size: 1rem;
  font-weight: 500;
  color: #222;
}

</style>