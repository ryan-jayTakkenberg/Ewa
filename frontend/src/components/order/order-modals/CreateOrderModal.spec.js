import { mount } from '@vue/test-utils';
import CreateOrderModal from './CreateOrderModal.vue';

let wrapper;
beforeEach(() => (wrapper = mount(CreateOrderModal)));

it('has order name field', () => {
    const nameInput = wrapper.find('input[placeholder="Order name"]');
    expect(nameInput.exists()).toBe(true);
});

it('has ordered from field', () => {
    const orderedFromInput = wrapper.find('input[placeholder="Ordered From"]');
    expect(orderedFromInput.exists()).toBe(true);
});

it('has team select field', () => {
    const teamSelect = wrapper.find('select#team');
    expect(teamSelect.exists()).toBe(true);
});

it('has order date field', () => {
    const orderDateInput = wrapper.find('input#orderDate');
    expect(orderDateInput.exists()).toBe(true);
});

it('has estimated delivery date field', () => {
    const estimatedDeliveryDateInput = wrapper.find('input#estimatedDeliveryDate');
    expect(estimatedDeliveryDateInput.exists()).toBe(true);
});

it('has products select field', () => {
    const productsSelect = wrapper.find('select#products');
    expect(productsSelect.exists()).toBe(true);
});

it('has add order button', () => {
    // Find the SolarButton component with the specified text
    const addProductButton = wrapper.findComponent({ name: 'SolarButton', props: { buttonText: 'Add' } });
    expect(addProductButton.exists()).toBe(true);
});

it('has create order button', () => {
    // Find the SolarButton component with the specified text
    const createOrderButton = wrapper.findComponent({ name: 'SolarButton', props: { buttonText: 'Create Order' } });
    expect(createOrderButton.exists()).toBe(true);
});
