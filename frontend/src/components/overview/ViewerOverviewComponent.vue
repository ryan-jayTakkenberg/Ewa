<script>
export default {

  name: "UserOverviewComponent",
  /* inject: ['viewerOverviewService'], */

  data() {
    return {
      viewerName: null,
      viewerTeam: null,
      viewerProjects: [],
      viewerReports: [],
      meetingTime: '11:30 - 12:30',
      meetingLocation: 'Warehouse 2',
      selectedMessages: [],

      projects: [
        { title: 'Project Green', team: '1' },
        { title: 'Project Blue', team: '2' },
        { title: 'Project Red', team: '1' },
        { title: 'Project Yellow', team: '3' },
      ],
      projectDescriptions: [
        { title: 'Planned from: 17/10/2023 to 25/10/2023' },
        { title: 'Planned from: 19/10/2023 to 23/10/2023' },
        { title: 'Planned from: 20/10/2023 to 27/10/2023' },
        { title: 'Planned from: 25/10/2023 to 31/10/2023' },
      ],
      sampleMessages: [
        {
          sender: 'Admin',
          content: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam distinctio, ducimus, fugiat incidunt natus non porro quasi qui recusandae tempora tempore vero vitae voluptatibus! Dolorem esse nemo soluta voluptatem voluptatum.'
        },
        {
          sender: 'Admin',
          content: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam distinctio, ducimus, fugiat incidunt natus non porro quasi qui recusandae tempora tempore vero vitae voluptatibus! Dolorem esse nemo soluta voluptatem voluptatum.'
        },
        {
          sender: 'Warehouse 2',
          content: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam distinctio, ducimus, fugiat incidunt natus non porro quasi qui recusandae tempora tempore vero vitae voluptatibus! Dolorem esse nemo soluta voluptatem voluptatum.'
        },
      ],
      }
    },

  mounted() {
    // this.fetchData();
  },

  methods: {

    // async fetchData() {
    //
    //   this.viewerName = await this.viewerOverviewService.fetchViewerName(this.viewerId);
    //
    //   this.viewerTeam = await this.viewerOverviewService.fetchViewerTeam(this.viewerId);
    //
    //   this.viewerProjects = await this.viewerOverviewService.fetchViewerProjects(this.viewerId);
    //
    //   this.viewerReports = await this.viewerOverviewService.fetchViewerReports(this.viewerId);
    // },

    /* async postReport() {

      const reportData = {
        // Report data
      };

      const postedReport = await this.viewerOverviewService.postViewerReports(this.viewerId, reportData);
      console.log('Posted Report:', postedReport);
    },

    */

    getRandomColor() {
      const colors = ['#00d315', '#ff0000'];
      const randomIndex = Math.floor(Math.random() * colors.length);
      return colors[randomIndex];
    },
    toggleSelected(index) {
      if (this.selectedMessages.includes(index)) {
        this.selectedMessages = this.selectedMessages.filter((item) => item !== index);
      } else {
        this.selectedMessages.push(index);
      }
    },
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
    },
  },

}

</script>

<template>

  <!--- Persona ---------------------------------------------------------------------------------->
  <div class="personaContainer">

    <div class="welcomeContainer">
      <h1>Hi {{ viewerName }}.</h1>
      <p class="description">This is an overview of information that is important to you.</p>
    </div>
    <div class="profilePicContainer">
      <div class="profilePic"></div>
    </div>

  </div>

  <!--- Agenda ---------------------------------------------------------------------------------->
  <div class="sectionContainer agenda">

    <div class="dateContainer">
      <p class="dayOfTheWeek">{{ dayOfTheWeek }}</p>
      <p class="dayOfTheWeekNum">{{ numberOfTheDay }}</p>
    </div>

      <div class="insightContainer">
        <p class="medium"> 1 meeting today:</p>
        <div class="meetingWrapper">
          <span class="material-symbols-outlined">schedule</span>
          <div> {{ meetingTime }} </div>
        </div>
        <div class="meetingWrapper">
          <span class="material-symbols-outlined">location_on</span>
          <div> {{ meetingLocation }} </div>
        </div>

      </div>

  </div>

  <!--- projects ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <h1 class="sectionTitle">Ongoing Projects</h1>
    <div class="projectContainer">

      <div class="projectWrapper" v-for="(project, index) in projects" :key="index">
        <div class="projectHeader">
          <div class="projectTitle"> {{ project.title }}</div>
          <div class="statusWrapper">
            <div class="projectStatus"> Status: </div>
            <div :style="{ backgroundColor: getRandomColor() }" class="statusColor"></div>
          </div>
        </div>
        <div class="projectDescription">{{ projectDescriptions[index].title }}<br>
              Team: {{ project.team }}</div>
        <p>Click for more details</p>
      </div>

    </div>
  </div>

  <!--- messages ---------------------------------------------------------------------------------->
  <div class="sectionContainer">

    <div class="messageHeader">
      <h1 class="sectionTitle">Messages</h1>
      <div class="buttonContainer">
        <button class="deleteMessage">
          <span class="material-symbols-outlined button">delete</span>
        </button>
        <button class="filterMessage">
          <span class="material-symbols-outlined button">filter_alt</span>
        </button>
      </div>
    </div>

    <div class="messageContainer">
      <div
          class="messageWrapper"
          v-for="(message, index) in sampleMessages"
          :key="index"
          @click="toggleSelected(index)"
          :class="{ 'selected': selectedMessages.includes(index) }">
        <div class="messageSender"> {{ message.sender }} </div>
        <div class="message"> {{ message.content }} </div>
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
  padding: 2rem 3rem 0 3rem;
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
  color: #aaa;
}

.profilePicContainer {
  height: 100px;
  width: 100px;
  background: #f5f5f5;
  border-radius: 50%;
}

.sectionContainer {
  width: 100%;
  padding: 2rem 3rem;
  border-bottom: 2px solid #e5e5e5;
}

.agenda {
  display: flex;
}

.dateContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 150px;
  height: 150px;
  border: 2px solid #e5e5e5;
  border-radius: 15px;
}

.dayOfTheWeek {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 0.8;
  color: #222;
}

.dayOfTheWeekNum {
  font-size: 3rem;
  font-weight: 800;
  line-height: 0.8;
  color: #222;
}

.insightContainer {
  display: flex;
  flex-direction: column;
  padding: 1rem;
}

.meetingWrapper {
  display: flex;
  align-items: center;
  justify-content: left;
  gap: 0.5rem;
  flex-direction: row;
}

.medium {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.sectionTitle {
  text-align: left;
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
}

.projectContainer {
  display: flex;
  gap: 1rem;
  width: 100%;
  height: auto;
  padding: 1rem 0.15rem;
  overflow-x: scroll;
  margin-top: 1rem;
}

.projectWrapper {
  min-width: 500px;
  width: auto;
  flex: 0 0 auto;
  background: #f5f5f5;
  border-radius: 5px;
  padding: 1rem;
  cursor: pointer;
}

.projectWrapper:hover {
  outline: 2px solid #222;
}

.projectHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.projectTitle {
  font-size: 1.2rem;
  font-weight: 700;
  color: #222;
}

.statusWrapper {
  display: flex;
  align-items: center;
}

.projectStatus {
  font-size: 1.2rem;
  font-weight: 400;
  color: #222;
}

.statusColor {
  height: 20px;
  width: 20px;
  border-radius: 50%;
  margin-left: 0.5rem;
}

p {
  margin-top: 1rem;
  font-weight: 300;
  color: #222;
}

.messageContainer {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  height: 500px;
  width: 50%;
  background: #f5f5f5;
  border-radius: 10px;
  overflow-y: scroll;
}

.messageHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 50%;
  margin-bottom: 1rem;
}

.messageWrapper {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  min-height: 150px;
  height: auto;
  flex: 0 0 auto;
  background: #fff;
  padding: 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.messageSender {
  width: 100%;
  font-weight: 600;
  color: #222;
  border-bottom: 1px solid #e5e5e5;
}

.selected {
  background-color: #e5e5e5;
}

.buttonContainer {
  display: flex;
  gap: 1rem;
}

button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  background: #f5f5f5;
  outline: none;
  cursor: pointer;
}

button:hover {
  outline: 2px solid #222;
}

</style>