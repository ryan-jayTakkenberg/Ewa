<template>
  <div class="box">
    <div class="content">
      <div class="content-header"></div>
      <div class="content-box">
        <div class="content-box-text">
          <table>
            <thead>
            <tr><th>List of all the Projects</th></tr>
            </thead>
            <tbody>
            <tr v-for="project in projectList" :key="project.name" @click="selectProject(project)" :class="{ 'selected': selectedProject === project }">
              {{"Name project: " + project.projectName + ", client: " + project.clientName +
            ", Date of installation: " + project.installDate + ", installed By: " + project.installTeam}}
            </tr>
            </tbody>
          </table>
          <button @click="showEditComponent = !showEditComponent" v-if="!selectedProject">Create a new Project</button>
          <button @click="removeProject()" v-if="selectedProject">Remove selected Project</button>
        </div>
      </div>
    </div>
  </div>
<ProjectEditComponent
  v-if="showEditComponent"
  @cancel="handleCancel"
  @add-project="addProject"
  @save-project="saveProject"
  :projectName="selectedProject ? selectedProject.projectName : ''"
  :clientName="selectedProject ? selectedProject.clientName : ''"
  :installDate="selectedProject ? selectedProject.installDate : ''"
  :installTeam="selectedProject ? selectedProject.installTeam : ''"
  :usedWarehouse="selectedProject ? selectedProject.usedWarehouse : ''"
  :notes="selectedProject ? selectedProject.notes : ''"
/>
</template>

<script>
import {Project} from "@/models/project";
import ProjectEditComponent from "@/components/manage/ProjectEditComponent";
export default {
  name: "ProjectDetailComponent",
  components: {
    ProjectEditComponent
  },
  data(){
    return{
      projectList: [],
      selectedProject: null,
      showEditComponent: false,
    }
  },
  created() {
    let amountOfProjects = 3;

    for (let i = 0; i < amountOfProjects; i++){
      const newProject = Project.createSampleProject()
      this.projectList.push(newProject);
    }
  },
  methods: {
    handleCancel(){
      this.showEditComponent = false;
      this.selectedProject = null;
    },
    selectProject(project){
      if (this.selectedProject === project){
        this.handleCancel();
      } else {
        this.selectedProject = project;
        this.showEditComponent = true;
      }
    },
    removeProject(){
      const index = this.projectList.indexOf(this.selectedProject);
      if (index !== -1){
        this.projectList.splice(index, 1)
        this.handleCancel();
      }
    },
    addProject(newProjectData) {
      const projectName = newProjectData.projectName;
      const clientName = newProjectData.clientName;
      const installDate = newProjectData.installDate;
      const installTeam = newProjectData.installTeam;
      const usedWarehouse = newProjectData.usedWarehouse;
      const notes = newProjectData.notes
      const newProject = new Project(projectName, clientName, installDate, installTeam, usedWarehouse, notes);
      this.projectList.push(newProject);
      this.handleCancel();
    },
    saveProject(updateProjectData) {
      if (this.selectedProject) {
        this.selectedProject.projectName = updateProjectData.projectName;
        this.selectedProject.clientName = updateProjectData.clientName;
        this.selectedProject.installDate = updateProjectData.installDate;
        this.selectedProject.installTeam = updateProjectData.installTeam;
        this.selectedProject.usedWarehouse = updateProjectData.usedWarehouse;
        this.selectedProject.notes = updateProjectData.notes;
      }
    }
  }
}
</script>

<style scoped>
.box{
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
  margin: 15px 0 30px 0;
}

.content-header{
  height: 175px;
  background: url("../../../static/images/manageDetailHeader.jpg") center no-repeat;
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

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

table{
  border-collapse: collapse;
  width: 100%;
}

tr{
  border: 1px solid black;
  padding: 10px;
}

tr:hover{
  color: #818181;
  cursor: pointer;
}

.selected{
  background: #c7d02c;
}
</style>