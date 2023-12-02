<template>
  <SolarTitle class="header" page-title="Project"></SolarTitle>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarSearchbar class="ml-2" place-holder="Search For Projects" @input="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Add Project" @click="showCreateProject = true"></SolarButton>
      </div>

      <SolarTable :columns="['Project', 'Installation date', 'notes', 'assigned Team']">
        <tr class="tableRow" v-for="(project) in projects" :key="project.projectId">
          <td class="w-4 p-4">
          </td>
          <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
            <div class="pl-3">
              <div class="text-base font-semibold">{{ project.projectName }}</div>
              <div class="font-normal text-gray-500">{{ project.clientName }}</div>
            </div>
          </th>
          <td class="px-6 py-4">{{project.installDate}}</td>
          <td class="px-6 py-4">{{project.notes}}</td>
          <td class="px-6 py-4">{{project.team.name}}</td>
          <td>
            <div class="editProjectButton" @click="openUpdateProject(project)" >Edit Project</div>
            <div class="deleteProjectButton" @click="deleteProject(project)">Delete Project</div>
          </td>
        </tr>
      </SolarTable>
    </div>
  </div>
  <CreateProject
      v-if="showCreateProject"
      @close-pop-up="closePopUp"
      @create-project="createProject"
  />
  <UpdateProject
      v-if="showUpdateProject"
      :project="selectedProject"
      @close-pop-up="closePopUp"
      @update-project="updateProject"
  />

</template>

<script>
import SolarTitle from "@/components/general/SolarTitle";
import SolarSearchbar from "@/components/general/SolarSearchbar";
import SolarButton from "@/components/general/SolarButton";
import SolarTable from "@/components/general/SolarTable";
import CreateProject from "@/components/project/projectPopUps/CreateProject";
import UpdateProject from "@/components/project/projectPopUps/UpdateProject";

export default {
  name: "ProjectOverview",
  inject: ['projectService'],
  components: {
    UpdateProject,
    SolarTitle,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    CreateProject,
  },
  data() {
    return {
      projects: [],
      selectedProject: null,
      showCreateProject: false,
      showUpdateProject: false,
    };
  },
  async created() {
    await this.getProjectList();
  },

  computed: {
    filteredProjects() {
      return this.projects.filter(p => {
        return Object.keys(p).some(key => `${p[key]}`.toLowerCase().includes(this.inputValue));
      })
    },
  },
  methods: {
    async createProject(newProject){
      try {
        await this.projectService.asyncSaveProject(newProject)
        await this.getProjectList();
      }catch (error){
        console.error("Error occurred during creation of new Project", error)
      }
    },
    async updateProject(project){
      try {
        await this.projectService.asyncUpdateProject(project)
        await this.getProjectList();
      } catch (error){
        console.error("Error occurred during saving of existing project", error)
      }
    },
    async deleteProject(project){
      // Use the browser-native confirmation dialog
      const confirmed = window.confirm("Are you sure you want to delete the project?");

      if (confirmed) {
        try {
          await this.projectService.asyncDeleteById(project.id);
          await this.getProjectList();
        } catch (error){
          console.error("Error occurred during deleting process", error)
        }
      }
    },
    async getProjectList(){
      try {
        this.projects = await this.projectService.asyncFindAll()
      } catch (error){
        console.error("Error occurred while getting the data from the backend", error)
      }
    },
    openUpdateProject(project){
      this.selectedProject = project;
      this.showUpdateProject = true;
    },
    closePopUp(){
      this.showCreateProject = false;
      this.showUpdateProject = false;
    },
    handleInputValueChange(value) {
      this.inputValue = value;
    },
  }
}
</script>

<style scoped>

.header {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
  color: #C7D02C;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  background-color: white;
}

.action-row {
  display: flex;
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}

.tableRow{
  background-color: white;
  border-bottom-width: 1px;
}

.tableRow:hover{
  background-color: rgb(249 250 251);
}

.editProjectButton{
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.editProjectButton:hover{
  text-decoration-line: underline;
}

.deleteProjectButton{
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.deleteProjectButton:hover{
  text-decoration-line: underline;
}
</style>
