<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import DropdownMenuButtonComponent from "@/components/general/SolarDropdownMenuButton.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import DropdownMenuItemComponent from "@/components/general/SolarDropdownMenuItem.vue";
import EditUserModal from "@/components/modals/EditUserModal.vue";

export default {
  name: "UsersComponent",
  components: {
    DropdownMenuItemComponent,
    TitleComponent,
    DropdownMenuButtonComponent,
    SearchBarComponent,
    ButtonComponent,
    EditUserModal,
  },
  data() {
    return {
      isEditUserModalOpen: false,  // Added data property to control modal visibility
    };
  },
  methods: {
    openEditUserModal() {
      this.isEditUserModalOpen = true;
    },
    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
  }
}
</script>

<template>
  <div class="header">
    <TitleComponent page-title="Users"></TitleComponent>
  </div>

  <div class="body">
    <div class="body-container">
      <EditUserModal v-if="isEditUserModalOpen" :on-close="closeEditUserModal"></EditUserModal>
      <div class="table-header">
        <!-- Action Dropdown Button -->
        <DropdownMenuButtonComponent button-text="Action">
          <DropdownMenuItemComponent text-menu-item="Edit Users"></DropdownMenuItemComponent>
          <DropdownMenuItemComponent text-menu-item="Delete Users"></DropdownMenuItemComponent>
        </DropdownMenuButtonComponent>

        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Users" class="ml-auto"></SearchBarComponent>
        <ButtonComponent button-text="Add User" on-click="OpenCreateUserModal"></ButtonComponent>

      </div>

      <table class="w-full text-sm text-left text-gray-500">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 ">
        <tr>
          <th scope="col" class="px-6 py-3"></th>
          <th scope="col" class="px-6 py-3">Name</th>
          <th scope="col" class="px-6 py-3">Function</th>
          <th scope="col" class="px-6 py-3">Status</th>
          <th scope="col" class="px-6 py-3">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr class="bg-white border-b hover:bg-gray-50 ">
          <td class="w-4 p-4">
            <div class="flex items-center">
              <input id="checkbox-table-search-1" type="checkbox"
                     class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2 ">
              <label for="checkbox-table-search-1" class="sr-only">checkbox</label>
            </div>
          </td>
          <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap ">
            <div class="pl-3">
              <div class="text-base font-semibold">Persoon 1</div>
              <div class="font-normal text-gray-500">test@solar-sedum.com</div>
            </div>
          </th>
          <td class="px-6 py-4">
            Employee Warehouse 69
          </td>
          <td class="px-6 py-4">
            <div class="flex items-center">
              <div class="h-2.5 w-2.5 rounded-full bg-green-500 mr-2"></div>
              Online
            </div>
          </td>
          <td class="px-6 py-4">
            <!-- Modal toggle -->
            <div @click="openEditUserModal" type="button" class="font-medium text-blue-600 hover:underline">Edit user
            </div>
          </td>
        </tr>
        <tr class="bg-white border-b hover:bg-gray-50">
          <td class="w-4 p-4">
            <div class="flex items-center">
              <input id="checkbox-table-search-2" type="checkbox"
                     class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2 ">
              <label for="checkbox-table-search-2" class="sr-only">checkbox</label>
            </div>
          </td>
          <th scope="row"
              class="flex items-center px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
            <div class="pl-3">
              <div class="text-base font-semibold">Persoon 2</div>
              <div class="font-normal text-gray-500">test@sedum-solar.com</div>
            </div>
          </th>
          <td class="px-6 py-4">
            Admin
          </td>
          <td class="px-6 py-4">
            <div class="flex items-center">
              <div class="h-2.5 w-2.5 rounded-full bg-green-500 mr-2"></div>
              Online
            </div>
          </td>
          <td class="px-6 py-4">
            <!-- Modal toggle -->
            <div @click="openEditUserModal" type="button" class="font-medium text-blue-600 hover:underline">Edit user
            </div>
          </td>
        </tr>


        <tr class="bg-white border-b hover:bg-gray-50 ">
          <td class="w-4 p-4">
            <div class="flex items-center">
              <input id="checkbox-table-search-2" type="checkbox"
                     class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2">
              <label for="checkbox-table-search-2" class="sr-only">checkbox</label>
            </div>
          </td>
          <th scope="row"
              class="flex items-center px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
            <div class="pl-3">
              <div class="text-base font-semibold">Persoon 3</div>
              <div class="font-normal text-gray-500">test@solar-sedum.com</div>
            </div>
          </th>
          <td class="px-6 py-4">
            Employee Warehouse 43
          </td>
          <td class="px-6 py-4">
            <div class="flex items-center">
              <div class="h-2.5 w-2.5 rounded-full bg-green-500 mr-2"></div>
              Online
            </div>
          </td>
          <td class="px-6 py-4">
            <!-- Modal toggle -->
            <div @click="openEditUserModal" type="button" class="font-medium text-blue-600 hover:underline">Edit user
            </div>
          </td>
        </tr>


        </tbody>
      </table>

      <!-- Edit user modal -->
      <div id="editUserModal" tabindex="-1" aria-hidden="true"
           class="fixed top-0 left-0 right-0 z-50 items-center justify-center hidden w-full p-4 overflow-x-hidden overflow-y-auto md:inset-0 h-[calc(100%-1rem)] max-h-full">
        <div class="relative w-full max-w-2xl max-h-full">
          <!-- Modal content -->
          <form action="#" class="relative bg-white rounded-lg shadow ">
            <!-- Modal header -->
            <div class="flex items-start justify-between p-4 border-b rounded-t">
              <h3 class="text-xl font-semibold text-gray-900 ">
                Edit user
              </h3>
              <button type="button"
                      class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ml-auto inline-flex justify-center items-center"
                      data-modal-hide="editUserModal">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                     viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                </svg>
                <span class="sr-only">Close modal</span>
              </button>
            </div>
            <!-- Modal body -->
            <div class="p-6 space-y-6">
              <div class="grid grid-cols-6 gap-6">
                <div class="col-span-6 sm:col-span-3">
                  <label for="first-name" class="block mb-2 text-sm font-medium text-gray-900 ">First
                    Name</label>
                  <input type="text" name="first-name" id="first-name"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5 "
                         placeholder="Bonnie" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="last-name" class="block mb-2 text-sm font-medium text-gray-900 ">Last
                    Name</label>
                  <input type="text" name="last-name" id="last-name"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5 "
                         placeholder="Green" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="email" class="block mb-2 text-sm font-medium text-gray-900 ">Email</label>
                  <input type="email" name="email" id="email"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="example@company.com" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="phone-number" class="block mb-2 text-sm font-medium text-gray-900 ">Phone
                    Number</label>
                  <input type="number" name="phone-number" id="phone-number"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="e.g. +(12)3456 789" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="department"
                         class="block mb-2 text-sm font-medium text-gray-900 ">Department</label>
                  <input type="text" name="department" id="department"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="Development" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="company"
                         class="block mb-2 text-sm font-medium text-gray-900 ">Company</label>
                  <input type="number" name="company" id="company"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="123456" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="current-password" class="block mb-2 text-sm font-medium text-gray-900 ">Current
                    Password</label>
                  <input type="password" name="current-password" id="current-password"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="••••••••" required="">
                </div>
                <div class="col-span-6 sm:col-span-3">
                  <label for="new-password" class="block mb-2 text-sm font-medium text-gray-900 ">New
                    Password</label>
                  <input type="password" name="new-password" id="new-password"
                         class="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-600 focus:border-blue-600 block w-full p-2.5    "
                         placeholder="••••••••" required="">
                </div>
              </div>
            </div>
            <!-- Modal footer -->
            <div class="flex items-center p-6 space-x-2 border-t border-gray-200 rounded-b">
              <button type="submit"
                      class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                Save all
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>

.table-header {
  display: flex;
  margin-bottom: 1rem /* 16px */;
}

.body {
  position: relative;
  overflow-x: auto;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem /* 16px */;
  background-color: white;
}

.header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}
</style>
