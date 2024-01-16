import { mount } from '@vue/test-utils';
import EditProductModal from './EditProductModal';
import Product from "@/models/product";

let wrapper;

const MODAL_TITLE_CONTAINS = 'Edit';
const PRODUCT_BUTTON_CONTAINS = 'Save';

beforeEach(() => wrapper = mount(EditProductModal, {
    propsData: {
        productInfos: [new Product(-1, 'name', 0, 'description')],
    }
}));

it('creates a proper page structure', () => {
    expect(wrapper.element.children.length,
        `main page starting with ${wrapper.element.tagName} has no child elements`)
        .toBeGreaterThan(0);
});

it('has name field', () => {
    // find the name input, but only when editing a single product!
    const field = wrapper.findAll('input')
        .find(searchbar => searchbar.element.id === 'name');

    expect(field?.exists(),
        `Name input field is missing!`)
        .toBe(true);
});

it('has price field', () => {
    // find the price input
    const field = wrapper.findAll('input')
        .find(searchbar => searchbar.element.id === 'price');

    expect(field?.exists(),
        `Price input field is missing!`)
        .toBe(true);
});

it('has description field', () => {
    // find the description input
    const field = wrapper.findAll('textarea')
        .find(searchbar => searchbar.element.id === 'description');

    expect(field?.exists(),
        `Description textarea field is missing!`)
        .toBe(true);
});

it('has edit button', () => {
    // find the edit button to click
    const confirmButton = wrapper.findAll('button')
        .find(button => button.element.innerHTML?.toLowerCase().includes(PRODUCT_BUTTON_CONTAINS.toLowerCase()));

    console.log(confirmButton);

    expect(confirmButton?.exists(),
        `missing edit product button`)
        .toBe(true);

    expect(confirmButton.element.disabled,
        `Edit product button should always be enabled!`)
        .toBeFalsy();
});

it('correct modal title', () => {
    // find the modal title
    const modalTitle = wrapper.findAll('h3')
        .find(th => th.element.innerHTML?.toLowerCase().includes(MODAL_TITLE_CONTAINS.toLowerCase()));

    expect(modalTitle?.exists(),
        `missing or incorrect modal title`)
        .toBe(true);
});
