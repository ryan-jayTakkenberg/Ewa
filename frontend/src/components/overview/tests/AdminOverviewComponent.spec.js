import { mount } from '@vue/test-utils';
import AdminOverviewComponent from '@/components/overview/AdminOverviewComponent.vue';
import Warehouse from '@/models/warehouse';
import Order from '@/models/order';

describe('AdminOverviewComponent.vue', () => {

    it('renders correctly', () => {
        const wrapper = mount(AdminOverviewComponent);
        expect(wrapper.exists()).toBe(true);
    });

    it('initializes with the correct data properties', () => {
        const wrapper = mount(AdminOverviewComponent);
        expect(wrapper.vm.productsSold).toBe('75');
        expect(wrapper.vm.unresolvedReports).toBe('0');
        expect(wrapper.vm.warehousesLowStock).toBe('3');
        // ... other data properties
    });

    // New third test
    it('correctly updates the status color for projects', () => {
        const wrapper = mount(AdminOverviewComponent);

        // Test with a future install date
        let futureProject = { installDate: new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toISOString() }; // Tomorrow's date
        let color = wrapper.vm.getStatusColor(futureProject);
        expect(color).toBe('#5DDB88'); // Green for future dates

        // Test with a past install date
        let pastProject = { installDate: new Date(new Date().getTime() - 24 * 60 * 60 * 1000).toISOString() }; // Yesterday's date
        color = wrapper.vm.getStatusColor(pastProject);
        expect(color).toBe('#FF0000'); // Red for past dates
    });


    it('filters users correctly in filteredUsers computed property', () => {
        const wrapper = mount(AdminOverviewComponent, {
            data() {
                return {
                    users: [{ id: 1, name: 'John', permissionLevel: 'ADMIN' }, { id: 2, name: 'Jane', permissionLevel: 'USER' }]
                };
            }
        });
        expect(wrapper.vm.filteredUsers).toEqual([{ id: 2, name: 'Jane', permissionLevel: 'USER' }]);
    });

    it('shows modal when showModal method is called with selected reports', async () => {
        const wrapper = mount(AdminOverviewComponent);
        wrapper.setData({ selectedReports: [{ id: 1, senderName: 'John Doe', body: 'Test report' }] });
        wrapper.vm.showModal();
        expect(wrapper.vm.modal).toBe(true);
    });

});
