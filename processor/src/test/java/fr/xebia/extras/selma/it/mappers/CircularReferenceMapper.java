/**
 * 
 */
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.CircularReference;

/**
 * Mapper for an bean with circular reference
 *
 */
@Mapper
public interface CircularReferenceMapper {

	CircularReference as(CircularReference in);

}
