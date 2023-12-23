import { mount } from '@vue/test-utils';
import LoginComponent from './LoginComponent.vue';
import axios from 'axios';

let wrapper;
jest.mock('axios');

// Define constants for element selectors
const GO_LOGIN_FORM_BUTTON = '.showLoginFormButton';
const EMAIL_INPUT_FIELD = '[name="email"]';
const PASSWORD_INPUT_FIELD = '[name="password"]';
const LOGIN_FORM = 'form';
const SUBMIT_LOGIN_BUTTON = '.loginButton';

beforeEach(() => {
    wrapper = mount(LoginComponent);
});

afterEach(() => {
    jest.restoreAllMocks();
});

it('creates a proper page structure', () => {
    // Check for the presence of specific elements
    expect(wrapper.find('.main').exists()).toBe(true);
    expect(wrapper.find('.backgroundImgContainer').exists()).toBe(true);
    expect(wrapper.find('.loginContainer').exists()).toBe(true);
});

it('go to login form button', () => {
    const showLoginFormButton = wrapper.find(GO_LOGIN_FORM_BUTTON);

    // Check if the button is visible and has the correct text
    expect(showLoginFormButton.isVisible()).toBe(true);
    expect(showLoginFormButton.text()).toBe('Login');

    // Simulate a click and check if the corresponding method is called
    showLoginFormButton.trigger('click');
    expect(wrapper.vm.showLoginForm).toBe(true);
});

it('has email input field', () => {
    const emailInput = wrapper.find(EMAIL_INPUT_FIELD);

    // Check for the presence of the label and its content
    expect(wrapper.find('label[for="email"]').exists()).toBe(true);
    expect(wrapper.find('label[for="email"]').text()).toBe('Email');

    // Check for the presence of the input field and its attributes
    expect(emailInput.exists()).toBe(true);
    expect(emailInput.element.type).toBe('text');
    expect(emailInput.element.required).toBe(true);

    // Simulate typing in the input field and check if the model is updated
    emailInput.setValue('test@example.com');
    expect(wrapper.vm.emailInput).toBe('test@example.com');
});

it('has password input field', () => {
    const passwordInput = wrapper.find(PASSWORD_INPUT_FIELD);

    // Check for the presence of the label and its content
    expect(wrapper.find('label[for="password"]').exists()).toBe(true);
    expect(wrapper.find('label[for="password"]').text()).toBe('Password');

    // Check for the presence of the input field and its attributes
    expect(passwordInput.exists()).toBe(true);
    expect(passwordInput.element.type).toBe('password');
    expect(passwordInput.element.required).toBe(true);

    // Simulate typing in the input field and check if the model is updated
    passwordInput.setValue('testpassword');
    expect(wrapper.vm.passwordInput).toBe('testpassword');
});

it('has submit login button', () => {
    const submitLoginButton = wrapper.find(SUBMIT_LOGIN_BUTTON);

    // Check for the presence of the button
    expect(submitLoginButton.exists()).toBe(true);
    expect(submitLoginButton.element.disabled, 'Login button is disabled').toBeFalsy();
});

it('displays error message on invalid login attempt', async () => {
    // Mock the postAPI function to simulate a failed login attempt
    axios.post.mockImplementationOnce(() => Promise.reject(new Error('Invalid login')));

    // Set initial state to show the login form
    wrapper.setData({ showLoginForm: true, emailInput: 'invalid@example.com', passwordInput: 'wrongPassword' });

    // Trigger the form submission
    await wrapper.find(LOGIN_FORM).trigger('submit.prevent');

    // Wait for the next tick to allow asynchronous operations to complete
    // https://vuejs.org/api/general.html#nexttick
    await wrapper.vm.$nextTick();

    // Check if the error message is displayed
    expect(wrapper.find('.errorMessage').text()).toBe('Invalid login, please try again');
});