<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import Project from "@/models/project";
import ProjectRowComponent from "@/components/project/ProjectRowComponent.vue";
import ProjectEditComponent from "@/components/project/ProjectEditComponent.vue";

export default {
  name: "ProjectOverview",
  components: {
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    ProjectRowComponent,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    ButtonComponent,
    ProjectEditComponent,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching projects
      projects: [...Project.projects],
      selectedProject: null,  // Track the selected user for editing
      isEditUserModalOpen: false,
      checkedProjects: [],
    };
  },
  created() {
    if (!this.projects?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!Project.fetching) {
          this.projects = [...Project.projects];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.filterValue to search in the table
    },
    openEditUserModal(project) {
      if (!project) {
        // No user selected
        return;
      }
      this.selectedProject = project;  // Set the selected user
      this.isEditUserModalOpen = true;  // Open the modal
    },
    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
    toggleCheckbox(project, isChecked) {
      if (isChecked) {
        this.checkedProjects.push(project.id);
      } else {
        this.checkedProjects = this.checkedProjects.filter(id => id !== project.id);
      }
      console.log(this.checkedProjects);
    },
    getSelectedProjects() {
      return this.projects.filter(project => this.checkedProjects.includes(project.id));
    }
  },
}
</script>

<template>
  <TitleComponent class="header" page-title="Projects"></TitleComponent>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem text-menu-item="Delete Project" @click="openDeleteProjectsModal"/>
        </SolarDropdownMenuButton>
        <SearchBarComponent place-holder="Search For Projects" @input="handleInputValueChange"/>
        <ButtonComponent class="ml-auto" button-text="Add Project" :onClick="openEditUserModal"></ButtonComponent>
      </div>

      <SolarTable :columns="['Project', 'Client', 'dateOfInstallation', 'Action']">
        <ProjectRowComponent
            v-for="(project, index) in projects"
            :key="index"
            :project="project"
            :isChecked="project.isChecked"
            @click-edit-user="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(project, $event)"><!-- Pass user and checkbox state -->
        </ProjectRowComponent>
      </SolarTable>
    </div>
  </div>
  <ProjectEditComponent v-if="isEditUserModalOpen" :on-close="closeEditUserModal" :project="selectedProject"/>
</template>

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

</style>
