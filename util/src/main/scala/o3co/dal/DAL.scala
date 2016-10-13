package o3co.dal

trait DAL 

/**
 * Base DAL trait with Slick JDBC Profile
 *
 * {{{
 *   trait MyDAL extends SlickDAL {
 *     import profile.api._
 *     class MyEntities extends Table[] ... 
 *   }
 *
 * }}}
 */
trait SlickDAL extends DAL with slick.WithJDBC {

}
