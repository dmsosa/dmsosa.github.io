import { ReactNode } from "react";
import { NavLink } from "react-router-dom";

function NavItem({icon=null, text="", url="", state="", activeClass="" } : {icon?: any, text?: string, url?: string, state?: string, activeClass?: string}) {
    return (
        <li className="nav-item">
            <NavLink className={activeClass} end state={state} to={url}>
                {icon && <i className={icon}></i>} {text}
            </NavLink>
        </li>
    )
}

export default NavItem;