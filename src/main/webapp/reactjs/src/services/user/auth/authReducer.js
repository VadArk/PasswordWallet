import {LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS, FAILURE} from './authTypes';

const initialState = {
    isLoggedIn: '',
    lastSuccess: '',
    lastUnsuccessful: ''
};

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case LOGIN_REQUEST:
            return {
                ...state
            };
        case LOGOUT_REQUEST:
            return {
                ...state
            };
        case SUCCESS: 
            return {
                isLoggedIn: action.payload,


            };
        case FAILURE: 
            return {
                isLoggedIn: action.payload.status,
                lastSuccess: action.payload.lastSuccess,
                lastUnsuccessful: action.payload.lastUnsuccessful
            };
        default:
            return state;
    }
};

export default reducer;