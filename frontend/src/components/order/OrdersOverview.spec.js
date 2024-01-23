import { mount } from '@vue/test-utils';
import orderOverview from './OrdersOverview';

let wrapper;

const SEARCHBAR_PLACEHOLDER_CONTAINS = 'search';
const TABLE_HEADER_CONTAINS = 'order';
const ACTION_BUTTON = 'Action';

beforeEach(() => wrapper = mount(orderOverview));

it('creates a proper page structure', () => {
    expect(wrapper.element.children.length,
        `main page starting with ${wrapper.element.tagName} has no child elements`)
        .toBeGreaterThan(0);
});

it('has searchbar', () => {
    // find the button to click
    const searchbar = wrapper.findAll('input')
        .find(searchbar => searchbar.element.placeholder?.toLowerCase().includes(SEARCHBAR_PLACEHOLDER_CONTAINS.toLowerCase()));

    expect(searchbar?.exists(), `Viewer is missing a searchbar`).toBe(true);
    expect(searchbar.element.disabled, `Searchbar element is disabled`).toBeFalsy();
});

it('has create button', () => {
    // Check if the user is an admin before looking for the button
    if (wrapper.vm.isAdmin()) {
        // find the SolarButton component
        const createButton = wrapper.findComponent({ name: 'SolarButton' });

        // assert that the SolarButton component exists
        expect(createButton.exists()).toBe(true);
    }
});


it('has action button', () => {
    // find the button to click
    const button = wrapper.findAll('button')
        .find(button => button.element.innerHTML?.toLowerCase().includes(ACTION_BUTTON.toLowerCase()));
    expect(button?.exists(), `missing action button`).toBe(true);
    expect(button.element.disabled, `Action button should be disabled`).toBeTruthy();
});

it('has table', () => {
    // find the button to click
    const orderTableHeader = wrapper.findAll('th')
        .find(th => th.element.innerHTML?.toLowerCase().includes(TABLE_HEADER_CONTAINS.toLowerCase()));
    expect(orderTableHeader?.exists(), `missing a order table`).toBe(true);
});
