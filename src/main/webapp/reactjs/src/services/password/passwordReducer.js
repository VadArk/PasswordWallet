import {SAVE_password_REQUEST, FETCH_password_REQUEST, UPDATE_password_REQUEST, DELETE_password_REQUEST, password_SUCCESS, PASS_FAILURE} from "./passwordTypes";

const initialState = {
    password: '', error: ''
};

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case SAVE_password_REQUEST || FETCH_password_REQUEST || UPDATE_password_REQUEST || DELETE_password_REQUEST:
            return {
                ...state
            };
        case password_SUCCESS:
            return {
                password: action.payload,
                error: ''
            };
        case PASS_FAILURE:
            return {
                password: '',
                error: action.payload
            };
        default: return state;
    }
};

export default reducer;