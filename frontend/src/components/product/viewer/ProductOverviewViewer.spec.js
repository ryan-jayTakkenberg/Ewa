import { mount } from '@vue/test-utils';
import ProductOverviewViewer from './ProductOverviewViewer';

let wrapper;

const SEARCHBAR_PLACEHOLDER_CONTAINS = 'search';
const TABLE_HEADER_CONTAINS = 'product';

beforeEach(function() {
    wrapper = mount(ProductOverviewViewer);
});

it('creates a proper page structure', function() {
    expect(wrapper.element.children.length,
        `main page starting with ${wrapper.element.tagName} has no child elements`)
        .toBeGreaterThan(0);
});

it('has searchbar', async function() {
    // find the button to click
    const searchbar = wrapper.findAll('input')
        .find(searchbar => searchbar.element.placeholder?.toLowerCase().includes(SEARCHBAR_PLACEHOLDER_CONTAINS.toLowerCase()));

    expect(searchbar?.exists(),
        `Viewer is missing a searchbar`)
        .toBe(true);

    expect(searchbar.element.disabled,
        `Searchbar element is disabled`)
        .toBeFalsy();
});

it('has table', async function() {
    // find the button to click
    const productTableHeader = wrapper.findAll('th')
        .find(th => th.element.innerHTML?.toLowerCase().includes(TABLE_HEADER_CONTAINS.toLowerCase()));

    expect(productTableHeader?.exists(),
        `Viewer is missing a product table`)
        .toBe(true);
});
