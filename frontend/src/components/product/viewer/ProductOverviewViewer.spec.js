import { mount } from '@vue/test-utils';
import ProductOverviewViewer from './ProductOverviewViewer';

let wrapper;

const SEARCHBAR_PLACEHOLDER_CONTAINS = 'search';

beforeEach(function() {
    wrapper = mount(ProductOverviewViewer);
});

it('creates a proper main page structure', function() {
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
