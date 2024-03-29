import { AxiosError } from "axios";

type TErrorData = {
    objectName: string,
    defaultMessage: string
}
type TApiError = {
    errors: TErrorData[]
}
export class ApiError {
    private error: AxiosError;
    private message;
    private _globallyHandled: boolean = false;
    constructor(error: AxiosError) {
        this.error = error;
        const statusCode = error.response? error.response.status : null;
        const errorData: TApiError | null = error.response? error.response.data as TApiError : null;
        if (errorData) {
            const { objectName, defaultMessage } = errorData.errors[0];
            this.message = `${objectName} due to ${defaultMessage}`;
        } else {
            switch (statusCode) {
                case 404:
                    this.message = "Resource not found";
                    break;
                case 403:
                    this.message = "Resource forbidden";
                    break;
                case 401:
                    this.message = "Please login to acces to this resource";
                    break;
                case 409:
                    this.message = "Resource conflict";
                    break;
                default:
                    this.message = "An exception ocurred while making the request"
            }
        }
        
    }
    public getMessage(): string {
        return this.message;
    }
    public getError(): AxiosError {
        return this.error;
    }
    public handleGlobally(): void {
        if (this._globallyHandled) {return};
        this._globallyHandled = true;
        console.log(this.error, this.message);
    }
}

//Global error handler
export function errorHandler(error: AxiosError) {
    const apiError = new ApiError(error);
    apiError.handleGlobally()
}