import { mount } from '@vue/test-utils';
import WarehouseOverview from "@/components/warehouse/WarehouseOverview";


describe('WarehouseOverview.vue', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(WarehouseOverview);
    });

    it('renders correctly', () => {
        expect(wrapper.exists()).toBe(true);
    });

    it('initializes with the correct properties', () => {
        expect(wrapper.vm.warehouses).toEqual([]);
        expect(wrapper.vm.selectedWarehouse).toBeNull();
        expect(wrapper.vm.showCreateWarehouse).toBe(false);
        expect(wrapper.vm.showUpdateWarehouse).toBe(false);
    });

    it('displays SolarTitle with the correct page title', () => {
        //arrange
        const pageTitle = 'Warehouse';
        //act
        const solarTitle = wrapper.findComponent({ name: 'SolarTitle' });

        //assert
        expect(solarTitle.exists()).toBe(true);
        expect(solarTitle.props('pageTitle')).toBe(pageTitle);
    });

    it('should set showCreateWarehouse to true when SolarButton is clicked', async () => {
        //arrange
        expect(wrapper.vm.showCreateWarehouse).toBe(false);

        //act
        // get the button by class name and click it
        await wrapper.find('.ml-auto.mr-2').trigger('click');

        //assert
        expect(wrapper.vm.showCreateWarehouse).toBe(true);
    });

    it('opens CreateWarehouse component when SolarButton is clicked', async () => {
        //arrange
        expect(wrapper.findComponent({ name: 'CreateWarehouse' }).exists()).toBe(false);

        //act
        await wrapper.find('.ml-auto.mr-2').trigger('click');

        //assert
        expect(wrapper.findComponent({ name: 'CreateWarehouse' }).exists()).toBe(true);
    });
});
