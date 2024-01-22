import { mount } from '@vue/test-utils'
import ProjectOverview from "@/components/project/ProjectOverview";

describe('ProjectOverview.vue', () => {
    let wrapper;

    beforeEach( () => {
        wrapper = mount(ProjectOverview);
    });

    it('renders component correctly', function () {
        expect(wrapper.exists()).toBe(true);
        console.log(wrapper.html())
    });

    it('should initialize with the correct properties', function () {
        expect(wrapper.vm.projects).toEqual([]);
        expect(wrapper.vm.selectedProject).toBeNull();
        expect(wrapper.vm.showCreateProject).toBe(false);
        expect(wrapper.vm.showUpdateProject).toBe(false);
        expect(wrapper.vm.inputValue).toEqual('')
    });

    it('should open create component on button click', function () {
        expect(wrapper.vm.showCreateProject).toBe(false);

        wrapper.find('.ml-auto.mr-2').trigger('click')

        expect(wrapper.vm.showCreateProject).toBe(true);
    });

})